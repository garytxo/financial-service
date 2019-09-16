package com.murray.financial.utils;

import com.murray.financial.domain.enums.AccountCurrency;
import com.murray.financial.domain.enums.AccountStatus;

/**
 * Test account numbers
 */
public class AccountNumber {


    public static final AccountNumber IBAN_NUMBER_INVALID = new AccountNumber("S020903200500041045040",AccountCurrency.EUR,AccountStatus.DELETED);
    public static final AccountNumber IBAN_NUMBER_GB = new AccountNumber("GB32ESSE40486562136016",AccountCurrency.GBP,AccountStatus.ACTIVE);
    public static final AccountNumber IBAN_NUMBER_SWISS = new AccountNumber("CH3908704016075473007",AccountCurrency.CHF,AccountStatus.ACTIVE);
    public static final AccountNumber IBAN_NUMBER_ES = new AccountNumber("ES1020903200500041045040",AccountCurrency.EUR,AccountStatus.ACTIVE);
    public static final AccountNumber IBAN_NUMBER_ES_2 = new AccountNumber("ES91 2100 0418 4502 0005 1332",AccountCurrency.EUR,AccountStatus.ACTIVE);
    public static final AccountNumber IBAN_NUMBER_IT = new AccountNumber("IT 60 X 05428 11101 000000123456",AccountCurrency.EUR,AccountStatus.ACTIVE);

    public static final AccountNumber IBAN_NUMBER_SWEDEN = new AccountNumber("SE6412000000012170145230",AccountCurrency.SEK,AccountStatus.ACTIVE);
    public static final AccountNumber IBAN_NUMBER_DK = new AccountNumber("DK5750510001322617",AccountCurrency.EUR,AccountStatus.ACTIVE);
    public static final AccountNumber BIC_CODE_DE = new AccountNumber("DEUTDEFF500",AccountCurrency.EUR,AccountStatus.ACTIVE);
    public static final AccountNumber BIC_CODE_INVALID = new AccountNumber("DEUTFFDEUTFF",AccountCurrency.EUR,AccountStatus.ACTIVE);


    private String number;
    private AccountCurrency accountCurrency;
    private AccountStatus status;

    public AccountNumber(String number, AccountCurrency accountCurrency, AccountStatus status) {
        this.number = number;
        this.accountCurrency = accountCurrency;
        this.status = status;
    }

    public String getNumber() {
        return number;
    }

    public AccountCurrency getAccountCurrency() {
        return accountCurrency;
    }

    public AccountStatus getStatus() {
        return status;
    }
}
