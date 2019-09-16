package com.murray.financial.service.impl;

import com.murray.financial.domain.enums.AccountCurrency;
import com.murray.financial.service.CurrencyConverter;

import java.math.BigDecimal;

/**
 * Third party service that can be used to make currency conversions.<br />
 * For further information on the API etc view <a href="https://exchangeratesapi.io/">exchangeratesapi</a>
 */
public class ExchangeRateCurrencyConverterImpl implements CurrencyConverter {

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal convertAmountTo(BigDecimal amount, AccountCurrency from, AccountCurrency to) {
        return null;
    }
}
