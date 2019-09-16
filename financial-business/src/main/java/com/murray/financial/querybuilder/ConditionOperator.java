package com.murray.financial.querybuilder;

/**
 * The condition operator  used to test for two or more conditions in a SELECT, INSERT, UPDATE, or DELETE statement.
 * All conditions must be met for a record to be selected.
 */
public enum ConditionOperator {

    AND(" AND "),
    OR(" OR ");

    private String symbol;

    ConditionOperator(String symbol) {
        this.symbol = symbol;
    }
}
