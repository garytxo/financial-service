package com.murray.financial.config;

import com.murray.financial.jobs.OperationalBankingJob;
import com.murray.financial.service.AccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.math.BigDecimal;

@Configuration
@EnableScheduling
public class JobsConfig {

    @Bean
    public OperationalBankingJob operationalBankingJob(final AccountService accountService,
                                                       @Value("${operational.banking.job.rate}") BigDecimal rate) {

        System.out.println("rate.... "+rate.toString());
        return new OperationalBankingJob(accountService,rate);

    }
}
