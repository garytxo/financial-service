package com.murray.financial.config;

import com.murray.financial.adapter.AccountsAdapter;
import com.murray.financial.adapter.impl.AccountsAdapterImpl;
import com.murray.financial.domain.repository.AccountTransferJPARespository;
import com.murray.financial.domain.repository.BackAccountJPARepository;
import com.murray.financial.service.AccountService;
import com.murray.financial.service.CurrencyConverter;
import com.murray.financial.service.impl.BankAccountServiceImpl;
import com.murray.financial.service.impl.ExchangeRateCurrencyConverterImpl;
import com.murray.financial.service.impl.InMemoryCurrencyConverterImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import java.util.Set;

@Configuration
public class ServiceConfig {

    private final AccountTransferJPARespository accountTransferJPARespository;

    private final BackAccountJPARepository backAccountJPARepository;

    public ServiceConfig(AccountTransferJPARespository accountTransferJPARespository, BackAccountJPARepository backAccountJPARepository, ConversionService conversionService) {
        this.accountTransferJPARespository = accountTransferJPARespository;
        this.backAccountJPARepository = backAccountJPARepository;
    }



    /**
     * Adapters responsible for converting the DTO to domain entities and vice-versa
     */
    @Bean
    public AccountsAdapter accountsAdapter(ConversionService conversionService) {

        return new AccountsAdapterImpl(conversionService, accountService());
    }

    /**
     * Account service which deals with all bank account and transfer logic
     */
    @Bean
    public AccountService accountService() {

        return new BankAccountServiceImpl(backAccountJPARepository, accountTransferJPARespository, currencyConverter());
    }

    @Bean
    public CurrencyConverter currencyConverter() {

        return new InMemoryCurrencyConverterImp();
    }
}
