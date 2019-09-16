package com.murray.financial.domain.entity;

import com.murray.financial.domain.enums.AccountStatus;
import com.murray.financial.domain.enums.TransactionType;
import com.murray.financial.utils.AccountNumber;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;


@SpringBootTest
public class BankAccountTest {


    @Test
    public void new_account_default_status_is_active() {

        BankAccount result = new BankAccount(AccountNumber.IBAN_NUMBER_SWISS.getNumber(), AccountNumber.IBAN_NUMBER_SWISS.getAccountCurrency());

        assertThat(result.getStatus(), is(equalTo(AccountStatus.ACTIVE)));
    }

    @Test
    public void new_account_createOn_date_matches_today() {

        BankAccount result = new BankAccount(AccountNumber.IBAN_NUMBER_SWISS.getNumber(), AccountNumber.IBAN_NUMBER_SWISS.getAccountCurrency());

        assertThat(result.getOpenedOn(), is(equalTo(LocalDate.now())));
    }

    @Test
    public void new_account_initial_balance_is_zero() {


        BankAccount result = new BankAccount(AccountNumber.IBAN_NUMBER_SWISS.getNumber(), AccountNumber.IBAN_NUMBER_SWISS.getAccountCurrency());

        assertThat(result.getBalance(), is(equalTo(BigDecimal.ZERO)));
    }

    @Test
    public void new_account_balance_credit_with_initial_deposit() {


        Transaction deposit = new Transaction(new BigDecimal(1000L), "Test");

        BankAccount result = new BankAccount(AccountNumber.IBAN_NUMBER_SWISS.getNumber(),
                AccountNumber.IBAN_NUMBER_SWISS.getAccountCurrency(), deposit);

        assertThat(result.getBalance(), is(equalTo(deposit.getAmount())));
        assertThat(result.getTransactions().size(), is(equalTo(1)));
        assertThat(result.getTransactions().iterator().next().getType(), is(TransactionType.CREDIT));

    }

    @Test
    public void account_balance_debited_after_transfer_to_account_account() {

        Transaction deposit = new Transaction(new BigDecimal(1000L), " Test ");
        BankAccount bankAccount = new BankAccount(AccountNumber.IBAN_NUMBER_SWISS.getNumber(),
                AccountNumber.IBAN_NUMBER_SWISS.getAccountCurrency(), deposit);

        assertThat(bankAccount.getBalance(), is(equalTo(deposit.getAmount())));

        Transaction transfer = new Transaction(new BigDecimal(200L).negate(), " test ");
        bankAccount.add(transfer);

        assertThat(bankAccount.getBalance(), is(equalTo(deposit.getAmount().add(transfer.getAmount()))));
        assertThat(bankAccount.getTransactions().size(), is(equalTo(2)));

        List<Transaction> transactions = new ArrayList<>(bankAccount.getTransactions());
        assertThat(transactions.get(0).getType(), is(TransactionType.CREDIT));
        assertThat(transactions.get(0).getAmount(), is(deposit.getAmount()));
        assertThat(transactions.get(1).getType(), is(TransactionType.DEBIT));
        assertThat(transactions.get(1).getAmount(), is(transfer.getAmount()));


    }


}
