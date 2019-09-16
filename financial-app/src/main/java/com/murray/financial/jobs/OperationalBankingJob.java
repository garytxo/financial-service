package com.murray.financial.jobs;

import com.murray.financial.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 *  ​Account​ balances are updated due to an Operational Banking Tax
 *  which are configured in the application properties
 */
public class OperationalBankingJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationalBankingJob.class);

    /**
     * {@link AccountService} which maintains the biz logic that will be executed
     * to update the account's balance
     */
    private final  AccountService accountService;
    /**
     * Tax rate used in the balance calculations
     */
    private final BigDecimal rate;

    public OperationalBankingJob(AccountService accountService, BigDecimal rate) {
        this.accountService = accountService;
        this.rate = rate;
    }


    @Transactional
    @Scheduled(cron = "${operational.banking.job.cron}")
    protected void runJob(){


        LOGGER.info("JOB running rate:{}",rate);
        accountService.updateAccountBalancesWith(rate);

    }
}
