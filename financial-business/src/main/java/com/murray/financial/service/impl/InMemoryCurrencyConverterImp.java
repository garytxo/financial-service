package com.murray.financial.service.impl;

import com.murray.financial.domain.enums.AccountCurrency;
import com.murray.financial.service.CurrencyConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * In memory coverter that should only be used for testing purposed
 */
public class InMemoryCurrencyConverterImp implements CurrencyConverter {


    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryCurrencyConverterImp.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal convertAmountTo(BigDecimal amount, AccountCurrency from, AccountCurrency to) {

        Map<AccountCurrency,BigDecimal> toRates = fixedRates.get(to);

        BigDecimal rate = toRates.get(from);

        LOGGER.info("convert {} {} to {} with rate {}",amount,from,to , rate);

        return amount.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }



    private static Map<AccountCurrency,Map<AccountCurrency,BigDecimal>> fixedRates= new HashMap<>();
    static {

        fixedRates.put(AccountCurrency.CHF,
                Stream.of(new Object[][] {
                        { AccountCurrency.CHF, BigDecimal.ONE },
                        { AccountCurrency.DKK, new BigDecimal(6.82d)},
                        { AccountCurrency.GBP, new BigDecimal(0.81d)},
                        { AccountCurrency.EUR, new BigDecimal(0.91d)},
                        { AccountCurrency.SEK, new BigDecimal(9.73d)},
                        { AccountCurrency.USD, new BigDecimal(1.01d)},
                }).collect(Collectors.toMap(data -> (AccountCurrency) data[0], data -> (BigDecimal)data[1])));

        fixedRates.put(AccountCurrency.USD,
                Stream.of(new Object[][] {
                        { AccountCurrency.CHF, new BigDecimal(0.98d)},
                        { AccountCurrency.DKK, new BigDecimal(6.73d)},
                        { AccountCurrency.GBP, new BigDecimal(0.80d)},
                        { AccountCurrency.EUR, new BigDecimal(0.90d)},
                        { AccountCurrency.SEK, new BigDecimal(9.59d)},
                        { AccountCurrency.USD, BigDecimal.ONE},
                }).collect(Collectors.toMap(data -> (AccountCurrency) data[0], data -> (BigDecimal)data[1])));

        fixedRates.put(AccountCurrency.EUR,
                Stream.of(new Object[][] {
                        { AccountCurrency.CHF, new BigDecimal(1.09d)},
                        { AccountCurrency.DKK, new BigDecimal(7.46d)},
                        { AccountCurrency.GBP, new BigDecimal(0.89d)},
                        { AccountCurrency.EUR, BigDecimal.ONE},
                        { AccountCurrency.SEK, new BigDecimal(10.64d)},
                        { AccountCurrency.USD, new BigDecimal(1.10d)},
                }).collect(Collectors.toMap(data -> (AccountCurrency) data[0], data -> (BigDecimal)data[1])));

        fixedRates.put(AccountCurrency.DKK,
                Stream.of(new Object[][] {
                        { AccountCurrency.CHF, new BigDecimal(0.14d)},
                        { AccountCurrency.DKK,  BigDecimal.ONE},
                        { AccountCurrency.GBP, new BigDecimal(0.11d)},
                        { AccountCurrency.EUR, new BigDecimal(0.13d)},
                        { AccountCurrency.SEK, new BigDecimal(1.42d)},
                        { AccountCurrency.USD, new BigDecimal(0.14d)},
                }).collect(Collectors.toMap(data -> (AccountCurrency) data[0], data -> (BigDecimal)data[1])));


        fixedRates.put(AccountCurrency.SEK,
                Stream.of(new Object[][] {
                        { AccountCurrency.CHF, new BigDecimal(0.10d)},
                        { AccountCurrency.DKK,  new BigDecimal(0.70d)},
                        { AccountCurrency.GBP, new BigDecimal(0.08d)},
                        { AccountCurrency.EUR, new BigDecimal(0.09d)},
                        { AccountCurrency.SEK, BigDecimal.ONE},
                        { AccountCurrency.USD, new BigDecimal(0.10d)},
                }).collect(Collectors.toMap(data -> (AccountCurrency) data[0], data -> (BigDecimal)data[1])));

        fixedRates.put(AccountCurrency.GBP,
                Stream.of(new Object[][] {
                        { AccountCurrency.CHF, new BigDecimal(1.22d)},
                        { AccountCurrency.DKK,  new BigDecimal(8.38d)},
                        { AccountCurrency.GBP, BigDecimal.ONE},
                        { AccountCurrency.EUR, new BigDecimal(1.12d)},
                        { AccountCurrency.SEK, new BigDecimal(11.9d)},
                        { AccountCurrency.USD, new BigDecimal(1.24d)},
                }).collect(Collectors.toMap(data -> (AccountCurrency) data[0], data -> (BigDecimal)data[1])));
    }



}
