package com.murray.financial.importer.csv;

import com.murray.financial.domain.entity.BankAccount;
import com.murray.financial.domain.entity.Transaction;
import com.murray.financial.domain.enums.AccountCurrency;
import com.murray.financial.service.AccountService;
import com.murray.financial.service.untils.IBANNumberUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.ParseBigDecimal;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

/**
 * CSV Accounts specific importer class that handle the importation of new
 */
public class CSVAccountsFileImporter extends CSVImporterBase<CSVAccountsFileImporter.AccountRow> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSVAccountsFileImporter.class);

    public CSVAccountsFileImporter(AccountService accountService) {
        super(accountService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadFileAndImport(final String inputRootDirectory) {


        File csvFile = findCsvFromResourceDirectory(inputRootDirectory)
                .orElseThrow(
                        () -> new IllegalArgumentException("Importation skipped: No import root directory found")
                );

        LOGGER.info("Converting csv file :{}", csvFile.getName());

        transformAndSave(convertFileToImporterObjects(csvFile, AccountRow.class));


    }

    /**
     * Transforms the {@link AccountRow} to a {@link BankAccount} entity and
     * saves.
     *
     * @param accounts list of account rows
     */
    private void transformAndSave(List<AccountRow> accounts) {

        LOGGER.info("Importing  {} accounts to database", accounts.size());

        for (AccountRow importAccount : accounts) {

            BankAccount bankAccount = bankAccount(importAccount);

            LOGGER.info("importing :{}", bankAccount);

            accountService.saveAccount(bankAccount);

        }
    }

    /**
     * Converts the {@link AccountRow} class into a {@link BankAccount} domain
     * entity that can be persist to the database
     *
     * @param importAccount {@link AccountRow}
     * @return {@link BankAccount}
     */
    private BankAccount bankAccount(final AccountRow importAccount) {

        AccountCurrency currency = AccountCurrency.valueOf(importAccount.getCurrency());
        String ibanNumber = IBANNumberUtils.createIBANNumber(currency.randomCountryCode());

        return new BankAccount(
                importAccount.getAccountId(),
                ibanNumber,
                currency,
                new Transaction(importAccount.getBalance(), " Imported Bank account"));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getImportFileName() {
        return ACCOUNT_FILE_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CellProcessor[] getProcessors() {

        final CellProcessor[] processors = new CellProcessor[]{
                new NotNull(new ParseLong()), // accountId
                new NotNull(new ParseBigDecimal()), // getBalance
                new NotNull(), // getCurrency
        };
        return processors;
    }

    /**
     * Represents on row of the Accounts csv file which contains the
     * account id, getBalance and getCurrency of a new account.
     */
    @Data
    public static class AccountRow {

        /**
         * account internal unique id
         */
        private Long accountId;
        /**
         * Account initial balance
         */
        private BigDecimal balance;

        /**
         * Account currency
         */
        private String currency;

        public AccountRow() {
        }

        public AccountRow(Long accountId, BigDecimal balance, String currency) {
            this.accountId = accountId;
            this.balance = balance;
            this.currency = currency;
        }

        @Override
        public String toString() {
            return "ImportAccount{" +
                    "accountId=" + accountId +
                    ", getBalance=" + balance +
                    ", getCurrency='" + currency + '\'' +
                    '}';
        }


    }
}
