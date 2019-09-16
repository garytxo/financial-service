package com.murray.financial.service;

import com.murray.financial.domain.entity.AccountTransfer;
import com.murray.financial.domain.entity.BankAccount;
import com.murray.financial.domain.entity.Transaction;
import com.murray.financial.domain.enums.AccountCurrency;
import com.murray.financial.domain.enums.AccountStatus;
import com.murray.financial.domain.repository.AccountTransferJPARespository;
import com.murray.financial.domain.repository.BackAccountJPARepository;
import com.murray.financial.domain.repository.query.*;
import com.murray.financial.exceptions.AccountCreationException;
import com.murray.financial.exceptions.TransferCreationException;
import com.murray.financial.querybuilder.Operator;
import com.murray.financial.querybuilder.OrderCondition;
import com.murray.financial.querybuilder.QueryCondition;
import com.murray.financial.service.impl.BankAccountServiceImpl;
import com.murray.financial.service.impl.InMemoryCurrencyConverterImp;
import com.murray.financial.service.untils.IBANNumberUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.murray.financial.utils.AccountNumber.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

@ActiveProfiles({"test"})
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Sql("classpath:test-data.sql")
public class AccountServiceITTest {

    private static final BigDecimal DKK_77 = new BigDecimal(77L);
    private static final BigDecimal ZERO = BigDecimal.ZERO;

    private CurrencyConverter currencyConverter = new InMemoryCurrencyConverterImp();


    @Autowired
    private BackAccountJPARepository backAccountRepository;

    @Autowired
    private AccountTransferJPARespository accountTransferJPARespository;

    private AccountService accountService;

    @Before
    public void setup() {

        accountService = new BankAccountServiceImpl(backAccountRepository, accountTransferJPARespository, currencyConverter);

    }

    @Test
    public void find_no_transfers_for_source_account_id(){


        BankAccount source = mockAccount(BigDecimal.ZERO, AccountStatus.ACTIVE, AccountCurrency.EUR);
        QueryCondition sourceIdCondition = new QueryCondition(TransferQueryField.SOURCE, Operator.EQUALS, source.getId());

        TransferSearch transferSearch = transferSearch(null,sourceIdCondition);

        List<TransferAccountResult> results = accountService.findTransfersBy(transferSearch);

        assertThat(results.size(),is(equalTo(0)));


    }

    @Test
    public void find_one_transfer_for_source_id_match(){

        String description = "transfer 700";
        BigDecimal transferAmt = new BigDecimal(700L);
        BigDecimal sourceOpenDeposit = new BigDecimal(1000L);
        BankAccount source = mockAccount(sourceOpenDeposit, AccountStatus.ACTIVE, AccountCurrency.EUR);
        BankAccount destination = mockAccount(BigDecimal.ZERO, AccountStatus.ACTIVE, AccountCurrency.EUR);
        accountService.createAccountTransfer(source, destination, transferAmt, description);


        QueryCondition sourceIdCondition = new QueryCondition(TransferQueryField.SOURCE, Operator.EQUALS, source.getIbanNumber());
        OrderCondition orderByAmount = new OrderCondition(TransferQueryField.DESTINATION);

        TransferSearch transferSearch = transferSearch(orderByAmount,sourceIdCondition);

        List<TransferAccountResult> results = accountService.findTransfersBy(transferSearch);

        assertThat(results.size(),is(equalTo(1)));

        assertThat(results.get(0).getAmount().longValue(),is(equalTo(transferAmt.longValue())));

        assertThat(results.get(0).getSourceAccountIbanNumber(),is(source.getIbanNumber()));

    }


    @Test(expected = TransferCreationException.class)
    public void throw_error_when_source_account_is_not_valid() throws Exception {

        BigDecimal transferAmt = BigDecimal.TEN;
        String description = "Test";
        BankAccount source = mockAccount(BigDecimal.ZERO, AccountStatus.DELETED, AccountCurrency.EUR);
        source.add(new Transaction(new BigDecimal(11111L), ""));
        BankAccount destination = mockAccount(BigDecimal.ZERO, AccountStatus.ACTIVE, AccountCurrency.EUR);

        accountService.createAccountTransfer(source, destination, transferAmt, description);


    }

    @Test
    public void execute_transfer_debits_source_and_credits_destination_account() {

        String description = "transfer 700";
        BigDecimal transferAmt = new BigDecimal(700L);
        BigDecimal sourceOpenDeposit = new BigDecimal(1000L);
        BankAccount source = mockAccount(sourceOpenDeposit, AccountStatus.ACTIVE, AccountCurrency.EUR);
        BankAccount destination = mockAccount(BigDecimal.ZERO, AccountStatus.ACTIVE, AccountCurrency.EUR);

        AccountTransfer transfer = accountService.createAccountTransfer(source, destination, transferAmt, description);

        assertThat(transfer, is(notNullValue()));

        AccountTransfer result = accountService.executeTransfer(transfer.getId());

        assertThat(result, is(notNullValue()));

        assertThat(result.getId(), is(equalTo(transfer.getId())));

        assertThat(result.getTimeStamp(), is(notNullValue()));

        assertThat(result.getTimeStamp().toLocalDate(), is(equalTo(LocalDate.now())));

        assertThat(result.getSource().getBalance().longValue(), is(300L));

        assertThat(result.getSource().getTransactions().size(), is(equalTo(2)));

        assertThat(result.getDestination().getBalance().longValue(), is(equalTo(transferAmt.longValue())));

        assertThat(result.getDestination().getTransactions().size(), is(equalTo(1)));


    }

    @Test
    public void transfer_from_eur_to_pound_converts_the_rate() {


        String description = "transfer 700 euro to GBP";
        BigDecimal sevenHundredInGBP = new BigDecimal(623L);
        BigDecimal transferAmt = new BigDecimal(700L);
        BigDecimal sourceOpenDeposit = new BigDecimal(1000L);
        BankAccount source = mockAccount(sourceOpenDeposit, AccountStatus.ACTIVE, AccountCurrency.EUR);
        BankAccount destination = mockAccount(BigDecimal.ZERO, AccountStatus.ACTIVE, AccountCurrency.GBP);

        AccountTransfer transfer = accountService.createAccountTransfer(source, destination, transferAmt, description);

        assertThat(transfer, is(notNullValue()));

        AccountTransfer result = accountService.executeTransfer(transfer.getId());

        assertThat(result, is(notNullValue()));

        assertThat(result.getId(), is(equalTo(transfer.getId())));

        assertThat(result.getTimeStamp(), is(notNullValue()));

        assertThat(result.getTimeStamp().toLocalDate(), is(equalTo(LocalDate.now())));

        assertThat(result.getSource().getBalance().longValue(), is(300L));

        assertThat(result.getSource().getTransactions().size(), is(equalTo(2)));

        assertThat(result.getDestination().getBalance().longValue(), is(equalTo(sevenHundredInGBP.longValue())));

        assertThat(result.getDestination().getTransactions().size(), is(equalTo(1)));
    }

    @Test
    public void return_new_transfer_when_source_is_valid_and_has_funds() throws Exception {

        BigDecimal transferAmt = BigDecimal.TEN;
        String description = "Test";
        BankAccount source = mockAccount(BigDecimal.ZERO, AccountStatus.ACTIVE, AccountCurrency.EUR);
        source.add(new Transaction(new BigDecimal(11111L), ""));
        BankAccount destination = mockAccount(BigDecimal.ZERO, AccountStatus.ACTIVE, AccountCurrency.EUR);

        AccountTransfer transfer = accountService.createAccountTransfer(source, destination, transferAmt, description);

        assertThat(transfer, is(notNullValue()));
        assertThat(transfer.getId(), is(notNullValue()));
        assertThat(transfer.getAmount().longValue(), is(equalTo(transferAmt.longValue())));
        assertThat(transfer.getTimeStamp(), is(nullValue()));

    }


    @Test
    public void generates_iban_when_iban_is_null() throws Exception {

        BankAccount result = accountService.saveBankAccount(null, AccountCurrency.EUR, ZERO);
        assertThat(result,is(notNullValue()));
        assertThat(result.getIbanNumber(),is(notNullValue()));

    }

    @Test(expected = AccountCreationException.class)
    public void throw_error_when_currency_is_null() throws Exception {

        accountService.saveBankAccount(IBAN_NUMBER_ES.getNumber(), null, ZERO);

    }

    @Test(expected = AccountCreationException.class)
    public void throw_error_when_iban_is_invalid() throws Exception {

        accountService.saveBankAccount(IBAN_NUMBER_INVALID.getNumber(), AccountCurrency.EUR, ZERO);

    }

    @Test
    public void update_active_account_status_to_inactive() throws Exception {

        BankAccount bankAccount = accountService.saveBankAccount(IBAN_NUMBER_IT.getNumber(), IBAN_NUMBER_IT.getAccountCurrency(), ZERO);

        bankAccount.setStatus(AccountStatus.DISABLED);


        BankAccount result = accountService.updateAccount(bankAccount);

        assertThat(result, notNullValue());
        assertThat(result.getCurrency(), is(equalTo(bankAccount.getCurrency())));
        assertThat(result.getStatus(), is(equalTo(AccountStatus.DISABLED)));

    }


    @Test
    public void find_account_matching_on_ibanNumber() throws Exception {

        BankAccount bankAccount = accountService.saveBankAccount(IBAN_NUMBER_SWISS.getNumber(),
                IBAN_NUMBER_SWISS.getAccountCurrency(), BigDecimal.TEN);

        QueryCondition ibanNumberQuery =
                new QueryCondition(BankAccountQueryField.IBAN_NUMBER, Operator.EQUALS, IBAN_NUMBER_SWISS.getNumber());

        BankAccountSearch bankAccountSearch = toBankAccountSearchWith(ibanNumberQuery);

        List<BankAccountResult> results = accountService.findBankAccountsBy(bankAccountSearch);

        assertThat(results, notNullValue());
        assertThat(results.size(), is(equalTo(1)));
        assertResultMatchEntity(results.get(0), bankAccount);

    }


    @Test
    public void return_empty_when_can_not_found_by_IBAN_number() throws Exception {

        QueryCondition ibanNumberQueryCondition =
                new QueryCondition(BankAccountQueryField.IBAN_NUMBER, Operator.EQUALS, "DK5750510001322123");

        BankAccountSearch bankAccountSearch = toBankAccountSearchWith(ibanNumberQueryCondition);

        List<BankAccountResult> accounts = accountService.findBankAccountsBy(bankAccountSearch);

        assertThat(accounts, notNullValue());
    }

    @Test
    public void find_account_matching_on_balance() throws Exception {


        BigDecimal sourceOpenDeposit = new BigDecimal(777L);
        BankAccount bankAccount = mockAccount(sourceOpenDeposit, AccountStatus.ACTIVE, AccountCurrency.DKK);

        QueryCondition balanceQueryCondition =
                new QueryCondition(BankAccountQueryField.BALANCE, Operator.EQUALS, sourceOpenDeposit);

        BankAccountSearch bankAccountSearch = toBankAccountSearchWith(balanceQueryCondition);

        List<BankAccountResult> results = accountService.findBankAccountsBy(bankAccountSearch);

        assertThat(results, notNullValue());
        assertThat(results.size(), is(equalTo(1)));
        assertResultMatchEntity(results.get(0), bankAccount);


    }

    @Test
    public void find_account_match_on_balance_and_status() throws Exception {


        BankAccount bankAccount = accountService.saveBankAccount(IBAN_NUMBER_SWEDEN.getNumber(),
                IBAN_NUMBER_SWEDEN.getAccountCurrency(), DKK_77);

        QueryCondition balanceQueryCondition =
                new QueryCondition(BankAccountQueryField.BALANCE, Operator.EQUALS, DKK_77);

        QueryCondition ibanNumberQueryCondition =
                new QueryCondition(BankAccountQueryField.IBAN_NUMBER, Operator.EQUALS, IBAN_NUMBER_SWEDEN.getNumber());


        QueryCondition statusQueryCondition =
                new QueryCondition(BankAccountQueryField.STATUS, Operator.EQUALS, IBAN_NUMBER_SWEDEN.getStatus().name());


        BankAccountSearch bankAccountSearch = toBankAccountSearchWith(balanceQueryCondition,ibanNumberQueryCondition,statusQueryCondition);

        List<BankAccountResult> results = accountService.findBankAccountsBy(bankAccountSearch);

        assertThat(results, notNullValue());
        assertThat(results.size(), is(equalTo(1)));
        assertResultMatchEntity(results.get(0), bankAccount);

    }

    @Test
    public void find_account_match_on_currency() throws Exception {


        BankAccount bankAccount = mockAccount(BigDecimal.ZERO, AccountStatus.ACTIVE, AccountCurrency.CHF);

        QueryCondition currencyQueryCondition =
                new QueryCondition(BankAccountQueryField.CURRENCY, Operator.EQUALS, AccountCurrency.CHF.name());


        BankAccountSearch bankAccountSearch = toBankAccountSearchWith(currencyQueryCondition);


        List<BankAccountResult> results = accountService.findBankAccountsBy(bankAccountSearch);

        assertThat(results, notNullValue());
        assertThat(results.size(), is(equalTo(1)));
        assertResultMatchEntity(results.get(0), bankAccount);

    }


    private void assertResultMatchEntity(BankAccountResult result, BankAccount bankAccount) {

        assertThat(result.status().name(), is(equalTo(bankAccount.getStatus().name())));
        assertThat(result.ibanNumber(), is(equalTo(bankAccount.getIbanNumber())));
        assertThat(result.currency().name(), is(equalTo(bankAccount.getCurrency().name())));
        assertThat(result.balance().longValue(), is(equalTo(bankAccount.getBalance().longValue())));


    }

    BankAccount mockAccount(BigDecimal balance,
                            AccountStatus status, AccountCurrency currency) {

        try {

            BankAccount bankAccount = accountService.saveBankAccount(IBANNumberUtils.createIBANNumber(currency.randomCountryCode()), currency, ZERO);

            // handle getStatus
            bankAccount.setStatus(status);

            if (Objects.nonNull(balance)) {

                Transaction deposit = new Transaction(balance, "test desposit");
                bankAccount.add(deposit);
            }

            return backAccountRepository.save(bankAccount);

        } catch (Exception e) {

            return null;
        }


    }

    BankAccountSearch toBankAccountSearchWith(QueryCondition ... conditions){

        BankAccountSearch search = new BankAccountSearch();

        for(QueryCondition condition:conditions){
            search.addCondition(condition);
        }

        return search;
    }

    TransferSearch transferSearch(OrderCondition orderCondition, QueryCondition ... conditions){

        TransferSearch search = new TransferSearch();

        for(QueryCondition condition:conditions){
            search.addCondition(condition);
        }
        if(Objects.nonNull(orderCondition)){
            search.setOrderCondition(orderCondition);;
        }

        return search;

    }


}
