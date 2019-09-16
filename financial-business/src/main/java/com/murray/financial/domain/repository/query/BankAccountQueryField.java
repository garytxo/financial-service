package com.murray.financial.domain.repository.query;

import com.murray.financial.querybuilder.QueryField;

/**
 * Set of {@link QueryField} that can be used to build custom query
 * for querying bank accounts.
 */
public class BankAccountQueryField<T extends Comparable> extends QueryField {

    public static final String NATIVE_QUERY = "SELECT b.open_on as openedOn, b.iban_number as ibanNumber," +
            " COALESCE(sum(t.amount),0) as balance , b.currency as currency , b.status as status " +
            " FROM bank_account b left join bank_account_transaction t on t.bank_account_id = b.id";

    public static final String GROUP_BY_ACCOUNT_ID=" b.id ";


    public static BankAccountQueryField<String> IBAN_NUMBER =
            new BankAccountQueryField<>("ibanNumber", "iban_number",
                    "iban_number", null);

    public static BankAccountQueryField<String> BALANCE =
            new BankAccountQueryField<>("balance", "balance",
                    "balance", "b.id");

    public static BankAccountQueryField<String> STATUS =
            new BankAccountQueryField<>("status", "status",
                    "status", null);

    public static BankAccountQueryField<String> CURRENCY =
            new BankAccountQueryField<>("currency", "currency",
                    "currency", null);

    public BankAccountQueryField(String name, String sqlColumnName, String conditionClause, String groupByColumnName) {
        super(name, sqlColumnName, conditionClause, groupByColumnName);
    }

}
