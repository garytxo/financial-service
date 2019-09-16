package com.murray.financial.utils;

import com.murray.financial.domain.enums.AccountCurrency;
import com.murray.financial.service.untils.IBANNumberUtils;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;


public class IBANNumberUtilsTest {


    /**
     * Introduced this test to ensure that all european countries are supported by the IBAN validator
     */
    @Test
    public void return_a_valid_number_for_each_supported_currency_country_code() {


        for (AccountCurrency currency : AccountCurrency.values()) {

            for (String country : currency.getCountryISOCodes()) {

                String result = IBANNumberUtils.createIBANNumber(country);

                assertThat(result, is(notNullValue()));
            }
        }


    }

    @Test
    public void return_new_random_euro_IBAN_account_number() {

        String result = IBANNumberUtils.createIBANNumber(AccountCurrency.EUR.randomCountryCode());

        assertThat(result, is(notNullValue()));

    }


    @Test
    public void return_false_when_BIC_valid_is_not_IBAN() {


        assertThat(IBANNumberUtils.isValidIBANumber(AccountNumber.BIC_CODE_DE.getNumber()), is(false));
    }

    @Test
    public void return_true_when_valid_IBAN_number() {


        assertThat(IBANNumberUtils.isValidIBANumber(AccountNumber.IBAN_NUMBER_GB.getNumber()), is(true));

    }

    @Test
    public void return_false_when_valid_IBAN_number() {


        assertThat(IBANNumberUtils.isValidIBANumber(AccountNumber.IBAN_NUMBER_INVALID.getNumber()), is(false));

    }

    @Test
    public void return_false_when_valid_IBAN_number_not_valid_BIC() {


        assertThat(IBANNumberUtils.isValidIBANumber(AccountNumber.IBAN_NUMBER_INVALID.getNumber()), is(false));

    }


}
