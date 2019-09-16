package com.murray.financial.importer.impl;

import com.murray.financial.importer.CSVImporterService;
import com.murray.financial.importer.csv.CSVAccountsFileImporter;
import com.murray.financial.importer.csv.CSVImporterBase;
import com.murray.financial.importer.csv.CSVTransfersFileImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@inheritDoc}
 */
public class CSVImporterServiceImpl implements CSVImporterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSVImporterServiceImpl.class);

    private final CSVAccountsFileImporter accountsFileImporter;

    private final CSVTransfersFileImporter transfersFileImporter;

    private final String inputRootDirectory;

    public CSVImporterServiceImpl(String inputRootDirectory, CSVAccountsFileImporter accountsFileImporter, CSVTransfersFileImporter transfersFileImporter) {
        this.accountsFileImporter = accountsFileImporter;
        this.transfersFileImporter = transfersFileImporter;
        this.inputRootDirectory = inputRootDirectory;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void checkAndImportFiles() {


        try {

            LOGGER.info("**** STARTING CSV IMPORTS ****");

            loadAndSave(accountsFileImporter);

            loadAndSave(transfersFileImporter);

            LOGGER.info("**** ENDED CSV IMPORTS ****");

        } catch (Exception e) {

            LOGGER.error("Aborting import following error occurred", e);
        }

    }


    void loadAndSave(CSVImporterBase fileImporter) {

        LOGGER.info("Starting {} importer", fileImporter.getImportFileName());
        fileImporter.loadFileAndImport(inputRootDirectory);
        LOGGER.info("Ended {} importer", fileImporter.getImportFileName());


    }
}
