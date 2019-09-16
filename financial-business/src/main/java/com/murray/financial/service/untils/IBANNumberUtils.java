package com.murray.financial.service.untils;

import com.murray.financial.domain.enums.AccountCurrency;
import org.apache.commons.lang3.RandomStringUtils;
import org.iban4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom IBAN account validator and IBAN account generator which is
 * used to create new accounts.
 */
public class IBANNumberUtils {


    private static final Logger LOGGER = LoggerFactory.getLogger(IBANNumberUtils.class);

    /**
     * Validate accountNumber to verify its a valid IBAN account number
     *
     * @param accountNumber String IBAN account number
     */
    public static boolean isValidIBANumber(final String accountNumber) {

        try {
            IbanUtil.validate(accountNumber);
            return true;
        } catch (IbanFormatException e) {

            LOGGER.error("Invalid IBAN Account", e);

            return false;
        }


    }

    /**
     * Create valid IBAN number from the {@link AccountCurrency}
     *
     * @param countryCode country code that corresponds to the curreny
     * @return String IBAN number
     */
    public static String createIBANNumber(final String countryCode) {

        try {

            LOGGER.debug("countryCode:{}", countryCode);

            Iban iban = new Iban.Builder().countryCode(CountryCode.getByCode(countryCode)).buildRandom();

            return iban.toFormattedString().replaceAll("\\s+", "");
        } catch (UnsupportedCountryException e) {

            LOGGER.warn("Not support {} will build fake account", countryCode);

            return toRandom(countryCode);
        }

    }

    /**
     * Generate a fake IBAN account number with first two chars of the
     * country code and 22 digits
     */
    private static String toRandom(final String countryCode) {

        int length = 22;
        boolean useLetters = false;
        boolean useNumbers = true;

        return countryCode.substring(0, 2).concat(
                RandomStringUtils.random(length, useLetters, useNumbers));

    }
}
