package com.murray.financial.domain.enums;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * List of ISO Currency codes supported by the application to date
 */
public enum AccountCurrency {

    //Swiss Franc
    CHF(Arrays.asList("LI", "CH")),
    //Denmark
    DKK(Arrays.asList("DK", "FO", "GL")),
    //Europe
    EUR(Arrays.asList("AD", "AT", "BE", "FI", "FR", "DE", "GR"
            , "IE", "IT", "LU", "MC", "NL", "PT", "SM", "SI", "ES", "ME")),
    //Great Britian
    GBP(Arrays.asList("GB")),
    //Sweden
    SEK(Arrays.asList("SE")),
    //USA (WHO DONT USE IBAN)
    USD(Arrays.asList("USA")),;

    private List<String> countryISOCodes;

    AccountCurrency(final List<String> countryISOCodes) {
        this.countryISOCodes = countryISOCodes;
    }

    /**
     * Return a randon country string for getCurrency
     */
    public String randomCountryCode() {
        int bound = countryISOCodes.size() > 1 ? countryISOCodes.size() - 1 : 1;
        return countryISOCodes
                .stream().skip(new Random().nextInt(bound)).findFirst().get();
    }

    public List<String> getCountryISOCodes() {
        return countryISOCodes;
    }

    /**
     * Converts the currency to its corresponding {@link AccountCurrency}
     * @param currency String currency
     * @return {@link AccountCurrency}
     * @throws  IllegalArgumentException when currency is invalid
     */
    public  static AccountCurrency toCurrency(final String currency){

        if(StringUtils.isEmpty(currency)){
            throw  new IllegalArgumentException("Currency field is required");
        }
        try{

            return AccountCurrency.valueOf(currency);

        }catch (NullPointerException np){

            throw  new IllegalArgumentException(String.format("Currency %s is not supported",currency));
        }

    }
}
