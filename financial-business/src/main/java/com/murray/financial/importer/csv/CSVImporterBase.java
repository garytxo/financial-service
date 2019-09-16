package com.murray.financial.importer.csv;

import com.murray.financial.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * CSV import base class
 */
public abstract class CSVImporterBase<T> {

    static final String ACCOUNT_FILE_NAME = "Accounts.csv";
    static final String TRANSFER_FILE_NAME = "Transfers.csv";
    private static final Logger LOGGER = LoggerFactory.getLogger(CSVImporterBase.class);


    final AccountService accountService;

    public CSVImporterBase(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Defines the importer file name that should exist in the defined input directory
     */
    public abstract String getImportFileName();


    /**
     * Sets up the supercsv processors used for reading the csv file rows
     */
    public abstract CellProcessor[] getProcessors();


    /**
     * Loads a specific csv file which can be found in the input root directory.
     * Each row is parsed and saved to the database to its corresponding tables.
     *
     * @param inputRootDirectory
     */
    public abstract void loadFileAndImport(final String inputRootDirectory);


    /**
     * Attempts to read the account subclass specific file that should
     * be loaded into the database
     *
     * @param inputRootDirectory String root input path
     * @return File
     */
    Optional<File> findCsvFromResourceDirectory(final String inputRootDirectory) {

        try {


            File inputsDirectory = getInputRootFolder(inputRootDirectory);

            if (!inputsDirectory.exists() || Objects.isNull(inputsDirectory.listFiles())) {
                throw new IllegalArgumentException("Importation skipped: No import root directory found");
            }

            for (File file : inputsDirectory.listFiles()) {

                if (fileNameEquals(file)) {

                    return Optional.of(file);

                }

            }

        } catch (NullPointerException npe) {

            LOGGER.error("No directory found for :" + inputRootDirectory, npe);
            return Optional.empty();

        }

        return Optional.empty();
    }

    /**
     * Checks if the file name equals the importer instance file name
     *
     * @param file {@link File}
     */
    private boolean fileNameEquals(final File file) {

        try {
            String fileNameTrimmed = trimFileExtension(file.getName());
            String importFileNameTrimmed = trimFileExtension(getImportFileName());

            return fileNameTrimmed.equals(importFileNameTrimmed);

        } catch (IndexOutOfBoundsException e) {
            LOGGER.error("Could not find the file extension:{}", file.getName());
            return false;
        }
    }

    /**
     * Trim the .csv  file extension from the file name
     *
     * @param fileName String file name to check
     * @return file name without the .csv extension
     */
    private String trimFileExtension(String fileName) {

        if (fileName.indexOf(".csv") == -1) {
            return fileName;
        }

        return fileName.substring(0, fileName.indexOf(".csv"));
    }

    /**
     * Returns the {@link File} that represents the importer root directory which is defined in the
     * application.properties.
     *
     * @param inputRootDirectory String root input path
     */
    private File getInputRootFolder(final String inputRootDirectory) {

        URL url = this.getClass().getClassLoader().getResource(inputRootDirectory);
        File file = null;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            file = new File(url.getPath());
        }

        return file;
    }

    /**
     * Generic class that is responsible for loading the csv file and converting each  row into
     * it specific sub class import class instance
     *
     * @param file   {@link File} csv file that contain the inform to load
     * @param tClass Pojo class which represents on row in the file and
     * @return list of tClass
     * @throws IOException
     */
    List<T> convertFileToImporterObjects(final File file, Class<T> tClass)  {

        if (!file.exists()) {
            LOGGER.warn("Skipping file {} does not exist", file.getName());
            return Collections.emptyList();
        }

        List<T> accounts = new ArrayList<>();

        try (ICsvBeanReader beanReader = new CsvBeanReader(new FileReader(file.getAbsoluteFile()),
                CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE)) {

            final String[] headers = beanReader.getHeader(true);

            final CellProcessor[] processors = getProcessors();

            T t;
            while ((t = beanReader.read(tClass, headers, processors)) != null) {
                accounts.add(t);
            }
        }catch (IOException e){

            LOGGER.error("Issue converting file:"+file.getName(),e);
        }

        return accounts;
    }
}
