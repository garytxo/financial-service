package com.murray.financial.importer.csv;

import com.murray.financial.service.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.File;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class CSVAccountsImporterTest {


    private static final String INPUT = "input";
    private static final String INPUT_INVALID = "asdasdasd";
    @Mock
    private AccountService accountService;

    private CSVAccountsFileImporter accountsImporter;

    @Before
    public void setup() {


        accountsImporter = new CSVAccountsFileImporter(accountService);
    }

    @Test
    public void exist_file_for_parsing() {

        assertThat(accountsImporter.findCsvFromResourceDirectory(INPUT).isPresent(), is(true));


    }

    @Test
    public void not_exist_file_for_parsing() {

        assertThat(accountsImporter.findCsvFromResourceDirectory(INPUT_INVALID).isPresent(), is(false));


    }

    @Test
    public void return_three_account_to_import_from_file() throws Exception {

        File file = accountsImporter.findCsvFromResourceDirectory(INPUT).orElse(null);
        List<CSVAccountsFileImporter.AccountRow> results = accountsImporter.convertFileToImporterObjects(file, CSVAccountsFileImporter.AccountRow.class);

        assertThat(results.size(), is(equalTo(3)));
    }
}
