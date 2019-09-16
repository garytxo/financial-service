package com.murray.financial.querybuilder;

import java.util.Objects;

/**
 * A query condition to be matched for all the entities we want to read from the database.
 * Defines three properties:
 * <ul>
 * <li>The field in form of a {@link QueryField}</li>
 * <li>An {@link Operator}</li>
 * <li>The value applied to the field according to the operator</li>
 * </ul>
 */
public class QueryCondition {

    /**
     * The field we want to compare
     */
    private QueryField field;

    /**
     * Operator or comparator between field and value
     */
    private Operator operator;

    /**
     * Value we want to compare the field against
     */
    private Object value;

    /**
     * Builds a condition.
     *
     * @param field    the affected field
     * @param operator Operator
     * @param value    the value applied to the field according to the operator
     */
    public QueryCondition(QueryField field, Operator operator, Object value) {
        this.field = Objects.requireNonNull(field);
        this.operator = Objects.requireNonNull(operator);
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public String toString() {
        return this.field.getName() + " " + this.operator.getSymbol() + " " + (this.value != null ? this.value.toString() : "null");
    }

    public QueryField getField() {
        return field;
    }

    public Operator getOperator() {
        return operator;
    }

    public Object getValue() {
        return value;
    }

    /**
     * The JPA native query clause which concats the column , operator and the
     * column as a named paramer.
     */
    public String toQueryClause() {

        return field.getConditionClause()
                + operator.getSymbol()
                + ":" + field.getSqlColumnName();
    }
}
