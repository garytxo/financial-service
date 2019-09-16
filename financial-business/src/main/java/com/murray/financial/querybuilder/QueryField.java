package com.murray.financial.querybuilder;

import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * A QueryField represents a property of an Entity (Bankaccount, Transaction...) which allows
 * to compare such property with a concrete value by using many comparators.
 */
public abstract class QueryField {

    /**
     * A name for the query field
     */
    private String name;

    /**
     * select sql column name which can be an alias
     */
    private String sqlColumnName;

    /**
     * The actual conditional clause which in the where would not
     * use the alias name is agregatted.
     */
    private String conditionClause;

    /**
     * boolean indicating if the condition is aggregated
     */
    private boolean isAggregated;

    /**
     * Column name defined in the aggregated clause
     */
    private String groupByColumnName;


    public QueryField(String name, String sqlColumnName, String conditionClause, String groupByColumnName) {
        this.name = name;
        this.sqlColumnName = sqlColumnName;
        this.conditionClause = conditionClause;
        this.groupByColumnName = groupByColumnName;

        this.isAggregated = !StringUtils.isEmpty(groupByColumnName);
    }

    /**
     * A name will be used to print the field in the log traces.
     */
    public String getName() {
        return name;
    }

    /**
     * The exact column name that should be used in the query
     */
    public String getSqlColumnName() {
        return sqlColumnName;
    }

    /**
     * The query condition clause
     */
    public String getConditionClause() {
        return conditionClause;
    }

    /**
     * Indicates of the field query condition is aggregated, such
     * as the getBalance.
     */
    public boolean isAggregated() {
        return isAggregated;
    }

    /**
     * Return the group by column name
     */
    public String getGroupByColumnName() {
        return groupByColumnName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QueryField)) return false;
        QueryField that = (QueryField) o;
        return isAggregated == that.isAggregated &&
                Objects.equals(name, that.name) &&
                Objects.equals(sqlColumnName, that.sqlColumnName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, sqlColumnName, isAggregated);
    }
}
