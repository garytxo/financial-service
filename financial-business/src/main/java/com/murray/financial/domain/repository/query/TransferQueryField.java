package com.murray.financial.domain.repository.query;

import com.murray.financial.querybuilder.QueryField;

/**
 * Set of {@link QueryField} that can be used to build custom query
 * for querying transfers.
 */
public class TransferQueryField<T extends Comparable> extends QueryField {

    public static final String NATIVE_QUERY = "select tr.id as transferId, tr.timestamp as transferSent , " +
            " source_account.iban_number as srcIbanNumber,dest_account.iban_number as destIbanNumber," +
            "tr.amount as amount " +
            "from account_transfer tr " +
            "left join bank_account  source_account on tr.source_account_id=source_account.id\n" +
            "left join bank_account  as dest_account on tr.destination_account_id=dest_account.id";

    public static final String GROUP_BY_TRANSFER_ID=" tr.id ";

    public static TransferQueryField<String> SOURCE =
            new TransferQueryField<>("transfer -> source_account_number",
                    "srcIbanNumber", "source_account.iban_number", null);

    public static TransferQueryField<String> DESTINATION =
            new TransferQueryField<>("transfer -> dectination_account_number", "destIbanNumber",
                    "dest_account.iban_number", null);

    public static TransferQueryField<String> STATUS =
            new TransferQueryField<>("transfer -> timestamp", "transferSent",
                    "tr.timestamp", null);


    public TransferQueryField(String name, String sqlColumnName, String conditionClause, String groupByColumnName) {
        super(name, sqlColumnName, conditionClause, groupByColumnName);
    }
}
