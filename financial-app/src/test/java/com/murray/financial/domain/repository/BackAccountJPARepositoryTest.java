package com.murray.financial.domain.repository;

import com.murray.financial.domain.entity.BankAccount;
import com.murray.financial.domain.entity.Transaction;
import com.murray.financial.domain.enums.AccountCurrency;
import com.murray.financial.domain.enums.AccountStatus;
import com.murray.financial.domain.enums.TransactionType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.murray.financial.utils.AccountNumber.IBAN_NUMBER_ES;
import static com.murray.financial.utils.AccountNumber.IBAN_NUMBER_GB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class BackAccountJPARepositoryTest {

    private static final BigDecimal OPEN_DEPOSIT = new BigDecimal(100L);
    private static final BigDecimal DKK_77 = new BigDecimal(77L);
    private static final BigDecimal ZERO = BigDecimal.ZERO;

    @Autowired
    private BackAccountJPARepository backAccountJPARepository;


    @Test
    public void new_account_balance_should_be_zero() throws Exception {


        BankAccount account = new BankAccount(IBAN_NUMBER_ES.getNumber(), IBAN_NUMBER_ES.getAccountCurrency(), null);

        BankAccount result = backAccountJPARepository.save(account);

        assertThat(result, notNullValue());
        assertThat(result.getBalance(), is(equalTo(BigDecimal.ZERO)));
        assertThat(result.getOpenedOn(), is(equalTo(LocalDate.now())));
        assertThat(result.getStatus(), is(equalTo(AccountStatus.ACTIVE)));
        assertThat(result.getCurrency(), is(equalTo(AccountCurrency.EUR)));
        assertThat(result.getTransactions().size(), is(equalTo(0)));


    }

    @Test
    public void save_account_with_opening_deposit_balance() throws Exception {

        Transaction deposit = new Transaction(OPEN_DEPOSIT,"Opening deposit");

        BankAccount account = new BankAccount(IBAN_NUMBER_GB.getNumber(),
                IBAN_NUMBER_GB.getAccountCurrency(), deposit);

        BankAccount result = backAccountJPARepository.save(account);

        assertThat(result, notNullValue());
        assertThat(result.getBalance(), is(equalTo(OPEN_DEPOSIT)));
        assertThat(result.getIbanNumber(), is(equalTo(IBAN_NUMBER_GB.getNumber())));
        assertThat(result.getOpenedOn(), is(equalTo(LocalDate.now())));
        assertThat(result.getStatus(), is(equalTo(AccountStatus.ACTIVE)));
        assertThat(result.getCurrency(), is(equalTo(IBAN_NUMBER_GB.getAccountCurrency())));
        assertThat(result.getTransactions().size(), is(equalTo(1)));

        Transaction transaction = account.getTransactions().iterator().next();

        assertThat(transaction.getAmount(), is(equalTo(OPEN_DEPOSIT)));
        assertThat(transaction.getType(), is(equalTo(TransactionType.CREDIT)));
        assertThat(transaction.getCreatedOn().toLocalDate(), is(equalTo(LocalDate.now())));
        assertThat(transaction.getId(), is(notNullValue()));

    }
}
