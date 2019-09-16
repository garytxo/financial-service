package com.murray.financial.importer.csv;

import com.murray.financial.service.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class CSVTransferImporterTest {


    private static final String INPUT = "input";
    private static final String INPUT_INVALID = "asdasdasd";
    @Mock
    private AccountService accountService;

    private CSVTransfersFileImporter transfersFileImporter;

    @Before
    public void setup() {


        transfersFileImporter = new CSVTransfersFileImporter(accountService);
    }

    @Test
    public void exist_file_for_parsing() {

        assertThat(transfersFileImporter.findCsvFromResourceDirectory(INPUT).isPresent(), is(true));


    }

    @Test
    public void not_exist_file_for_parsing() {

        assertThat(transfersFileImporter.findCsvFromResourceDirectory(INPUT_INVALID).isPresent(), is(false));


    }

    @Test
    public void parse_date_time_from_string() {

        String dateTime = "2018/11/01 09:03:56";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime formatDateTime = LocalDateTime.parse(dateTime, transfersFileImporter.timeFormatter());

        assertThat(formatDateTime, is(notNullValue()));
    }

    @Test
    public void return_three_account_to_import_from_file() throws Exception {

        File file = transfersFileImporter.findCsvFromResourceDirectory(INPUT).orElse(null);
        List<CSVTransfersFileImporter.TransferRow> results = transfersFileImporter.convertFileToImporterObjects(file,
                CSVTransfersFileImporter.TransferRow.class);

        assertThat(results.size(), is(equalTo(2)));
    }
}
