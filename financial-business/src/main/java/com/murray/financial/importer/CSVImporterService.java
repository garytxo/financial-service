package com.murray.financial.importer;

import com.murray.financial.importer.csv.CSVImporterBase;

/**
 * Checks specific CSV  files in a defined directory that should be imported once the
 * application is up and running
 */
public interface CSVImporterService {

    /**
     * Checks each sub class {@link CSVImporterBase} in
     * a specific order for files. Each importer then loads and inserts into the corresponding
     * database tables.
     */
    void checkAndImportFiles();
}
