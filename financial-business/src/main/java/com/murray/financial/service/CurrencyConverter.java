package com.murray.financial.service;

import com.murray.financial.domain.enums.AccountCurrency;

import java.math.BigDecimal;

/**
 * External service that handles currency conversions among difference currencies
 * supported in the application
 */
public interface CurrencyConverter {


    /**
     * Convert the amount from its original currency to another.
     * @param amount amount to covert
     * @param from the base or original currency the amount is stored in
     * @param to  the currency which to convert to
     * @return BigDecimal
     */
    BigDecimal convertAmountTo(final BigDecimal amount, AccountCurrency from, AccountCurrency to);
}
