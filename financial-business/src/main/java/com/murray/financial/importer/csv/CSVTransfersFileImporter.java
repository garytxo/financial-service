package com.murray.financial.importer.csv;

import com.murray.financial.domain.entity.AccountTransfer;
import com.murray.financial.domain.entity.BankAccount;
import com.murray.financial.service.AccountService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.ParseBigDecimal;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.time.ParseLocalDateTime;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * CSV Accounts specific importer class that handle the importation of new
 */
public class CSVTransfersFileImporter extends CSVImporterBase<CSVTransfersFileImporter.TransferRow> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSVTransfersFileImporter.class);


    public CSVTransfersFileImporter(AccountService accountService) {
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


        transformAndSave(convertFileToImporterObjects(csvFile, TransferRow.class));


    }

    private void transformAndSave(final List<TransferRow> transferRows) {


        LOGGER.info("Import {} {} to transfers to database", transferRows.size());

        for (TransferRow row : transferRows) {

            AccountTransfer transfer = toTransfer(row);

            accountService.persistTransfer(transfer);

            accountService.executeTransfer(transfer.getId());
        }
    }

    /**
     * Converts the {@link TransferRow} class into a {@link AccountTransfer} domain
     * entity that can be persist to the database
     *
     * @param row {@link TransferRow}
     * @return {@link AccountTransfer}
     */
    private AccountTransfer toTransfer(final TransferRow row) {


        BankAccount source = findAccountBy(row.source)
                .orElseThrow(() -> new IllegalArgumentException("No source  account id found for " + row.source));

        BankAccount destination = findAccountBy(row.destination)
                .orElseThrow(() -> new IllegalArgumentException("No destination account id found for " + row.description));

        return new AccountTransfer(source, destination, row.amount,
                row.description, row.timestamp);


    }

    /**
     * Find the {@link BankAccount} by internal id number
     *
     * @param id
     */
    private Optional<BankAccount> findAccountBy(final Long id) {

        return accountService.findAccountById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getImportFileName() {
        return TRANSFER_FILE_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CellProcessor[] getProcessors() {

        final CellProcessor[] processors = new CellProcessor[]{
                new NotNull(new ParseLong()), // src_acount_id
                new NotNull(new ParseLong()), // dest_acount_id
                new NotNull(new ParseBigDecimal()), // amount
                new NotNull(), // description
                new NotNull(new ParseLocalDateTime(timeFormatter())), // timestamp
        };
        return processors;
    }

    /**
     * {@link DateTimeFormatter} defining the actual date time format of the timestamp column
     */
    DateTimeFormatter timeFormatter() {


        return DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    }


    /**
     * Represents on row of the Accounts csv file which contains the
     * account id, getBalance and getCurrency of a new account.
     */
    @Data
    public static class TransferRow {

        /**
         * Transfer account internal id
         */
        private Long source;


        /**
         * Destination account internal id
         */
        private Long destination;

        /**
         * Transfer amount
         */
        private BigDecimal amount;

        /**
         * Transfer description
         */
        private String description;

        /**
         * Transfer execution date
         */
        private LocalDateTime timestamp;

        public TransferRow() {
        }

        public TransferRow(Long source, Long destination, BigDecimal amount, String description, LocalDateTime timestamp) {
            this.source = source;
            this.destination = destination;
            this.amount = amount;
            this.description = description;
            this.timestamp = timestamp;
        }


    }
}
