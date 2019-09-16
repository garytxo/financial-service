package com.murray.financial.config;

import com.murray.financial.importer.CSVImporterService;
import com.murray.financial.importer.csv.CSVAccountsFileImporter;
import com.murray.financial.importer.csv.CSVTransfersFileImporter;
import com.murray.financial.importer.impl.CSVImporterServiceImpl;
import com.murray.financial.service.AccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CsvImporterConfig {


    private final AccountService accountService;

    public CsvImporterConfig(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * The main csv file importer service which call the other defined file importers in a specific order
     *
     * @param inputRootDirectory root input directory where the csv file are found,
     */
    @Bean
    public CSVImporterService csvImporterService(@Value("${importer.root.directory}") String inputRootDirectory) {

        return new CSVImporterServiceImpl(inputRootDirectory, accountsFileImporter(), transfersFileImporter());
    }

    /**
     * Account csv file importer
     */
    @Bean
    public CSVAccountsFileImporter accountsFileImporter() {

        return new CSVAccountsFileImporter(accountService);
    }

    /**
     * Trnasfers csv file importer
     */
    @Bean
    public CSVTransfersFileImporter transfersFileImporter() {

        return new CSVTransfersFileImporter(accountService);
    }
}
