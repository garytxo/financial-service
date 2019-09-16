package com.murray.financial.domain.repository.query;

import com.murray.financial.domain.entity.BankAccount;
import com.murray.financial.domain.enums.AccountCurrency;
import com.murray.financial.domain.enums.AccountStatus;
import com.murray.financial.querybuilder.SearchResultSet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents an {@link BankAccount} which is returned from the
 * {@link com.murray.financial.domain.repository.Impl.BankAccountJPACustomRepositoryImpl}
 */
public class BankAccountResult implements Serializable, SearchResultSet {


    public static final String BANK_ACCOUNT_RESULTS = "BankAccountResults";

    /**
     * Account opening date
     */
    private LocalDate openedOn;

    /**
     * Account unique IBAN Number
     */
    private String ibanNumber;

    /**
     * Account current getBalance
     */
    private BigDecimal balance;

    /**
     * Account {@link AccountCurrency}
     */
    private String currency;

    /**
     * Account {@link AccountStatus}
     */

    private String status;

    public BankAccountResult(LocalDate openedOn, String ibanNumber, BigDecimal balance,
                             String currency, String status) {
        this.openedOn = openedOn;
        this.ibanNumber = ibanNumber;
        this.balance = balance;
        this.currency = currency;
        this.status = status;
    }


    @Override
    public String resultSetObjectName() {
        return BANK_ACCOUNT_RESULTS;
    }


    @Override
    public String toString() {
        return "BankAccountResult{" +
                "getOpenedOn=" + openedOn +
                ", getIbanNumber='" + ibanNumber + '\'' +
                ", getBalance=" + balance +
                ", getCurrency='" + currency + '\'' +
                ", getStatus='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BankAccountResult)) return false;
        BankAccountResult that = (BankAccountResult) o;
        return Objects.equals(ibanNumber, that.ibanNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ibanNumber);
    }


    public LocalDate openedOn() {
        return openedOn;
    }


    public String ibanNumber() {
        return ibanNumber;
    }


    public BigDecimal balance() {
        return balance;
    }


    public AccountCurrency currency() {
        return AccountCurrency.valueOf(currency);
    }


    public AccountStatus status() {
        return AccountStatus.valueOf(status);
    }

}
