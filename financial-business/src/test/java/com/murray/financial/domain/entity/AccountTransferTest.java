package com.murray.financial.domain.entity;

import com.murray.financial.domain.enums.AccountCurrency;
import com.murray.financial.domain.enums.AccountStatus;
import com.murray.financial.exceptions.TransferCreationException;
import com.murray.financial.service.untils.IBANNumberUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;


public class AccountTransferTest {


    @Test(expected = TransferCreationException.class)
    public void error_when_source_account_is_not_active() {

        BigDecimal transferAmt = BigDecimal.TEN;
        String description = "Test";
        BankAccount source = mockAccount(BigDecimal.ZERO, AccountStatus.DISABLED, AccountCurrency.EUR);
        BankAccount destination = mockAccount(BigDecimal.ZERO, AccountStatus.ACTIVE, AccountCurrency.EUR);

        new AccountTransfer(source, destination, transferAmt, description);


    }

    @Test(expected = TransferCreationException.class)
    public void error_when_destinaton_account_is_not_active() {

        BigDecimal transferAmt = BigDecimal.TEN;
        String description = "Test";
        BankAccount source = mockAccount(BigDecimal.ZERO, AccountStatus.ACTIVE, AccountCurrency.EUR);
        BankAccount destination = mockAccount(BigDecimal.ZERO, AccountStatus.DELETED, AccountCurrency.EUR);

        new AccountTransfer(source, destination, transferAmt, description);


    }

    @Ignore("Disabled validation because it seems accounts can accumulated debt")
    @Test(expected = TransferCreationException.class)
    public void error_when_transfer_amount_is_negative_amount() {

        BigDecimal transferAmt = BigDecimal.ZERO;
        String description = "Test";
        BankAccount source = mockAccount(BigDecimal.ZERO, AccountStatus.ACTIVE, AccountCurrency.EUR);
        BankAccount destination = mockAccount(BigDecimal.ZERO, AccountStatus.ACTIVE, AccountCurrency.EUR);

        new AccountTransfer(source, destination, transferAmt, description);

    }

    @Ignore("Disabled validation because it seems accounts can accumulated debt")
    @Test(expected = TransferCreationException.class)
    public void error_when_transfer_amount_is_null_amount() {

        BigDecimal transferAmt = null;
        String description = "Test";
        BankAccount source = mockAccount(BigDecimal.ZERO, AccountStatus.ACTIVE, AccountCurrency.EUR);
        BankAccount destination = mockAccount(BigDecimal.ZERO, AccountStatus.ACTIVE, AccountCurrency.EUR);

        new AccountTransfer(source, destination, transferAmt, description);

    }

    @Ignore("Disabled validation because it seems accounts can accumulated debt")
    @Test(expected = TransferCreationException.class)
    public void error_when_source_account_balance_has_not_enough_funds() {

        BigDecimal transferAmt = BigDecimal.TEN;
        String description = "Test";
        BankAccount source = mockAccount(BigDecimal.ZERO, AccountStatus.ACTIVE, AccountCurrency.EUR);
        BankAccount destination = mockAccount(BigDecimal.ZERO, AccountStatus.ACTIVE, AccountCurrency.EUR);

        new AccountTransfer(source, destination, transferAmt, description);

    }

    @Test
    public void new_instance_when_source_account_balance_has_enough_funds() {

        BigDecimal transferAmt = BigDecimal.TEN;
        String description = "Test";
        BankAccount source = mockAccount(BigDecimal.ZERO, AccountStatus.ACTIVE, AccountCurrency.EUR);
        source.add(new Transaction(new BigDecimal(11111L), ""));
        BankAccount destination = mockAccount(BigDecimal.ZERO, AccountStatus.ACTIVE, AccountCurrency.EUR);

        AccountTransfer transfer = new AccountTransfer(source, destination, transferAmt, description);
        assertThat(transfer, is(notNullValue()));
        assertThat(transfer.getAmount(), is(equalTo(transferAmt)));


    }

    BankAccount mockAccount(BigDecimal balance,
                            AccountStatus status, AccountCurrency currency) {


        String ibanNumber = IBANNumberUtils.createIBANNumber(currency.randomCountryCode());
        BankAccount account = new BankAccount(ibanNumber, currency, new Transaction(balance, "test"));
        account.setStatus(status);


        return account;
    }
}
