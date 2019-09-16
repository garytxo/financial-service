package com.murray.financial.querybuilder;

import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Search query class that defines the search query fields and any option
 * query conditions that can be used in the query.<br/>
 *
 * @param <E> the JPA Entity class which defines the {@link javax.persistence.SqlResultSetMapping} that matches the {@link SearchResultSet}
 * @param <R> {@link SearchResultSet} which result mapping object returned with the query results.
 */
public abstract class SearchQuery<E extends SearchableEntity, R extends SearchResultSet> {

    private static final String WHERE = " WHERE ";
    private static final String HAVING = " HAVING ";
    private static final String GROUP_BY = " GROUP BY ";
    private static final String ORDER_BY = " ORDER BY ";
    /**
     * Predicate to indicate if there are any where Query Conditions
     */
    private Predicate<Set<QueryCondition>> whereCondition = queryConditions ->
            queryConditions.stream().anyMatch(qf -> !qf.getField().isAggregated());
    /**
     * Predicate to indicate if there are any having Query Conditions
     */
    private Predicate<Set<QueryCondition>> aggregateCondition = queryConditions ->
            queryConditions.stream().anyMatch(qf -> qf.getField().isAggregated());

    /**
     * Ordering condition for the results.
     */
    private OrderCondition orderCondition;

    /**
     * {@link ConditionOperator} used between multiple query condition
     * Default is AND
     */
    private ConditionOperator conditionOperator;

    /**
     * {@link QueryCondition} that will be used to filter out results.
     */
    private Set<QueryCondition> queryConditions;


    protected SearchQuery() {
        this.conditionOperator = ConditionOperator.AND;
        this.queryConditions = new HashSet<>();
    }

    /**
     * Return all the valid query conditions that are used in query genration
     */
    public Set<QueryCondition> queryConditions() {
        return queryConditions;
    }

    /**
     * Defines all the valid {@link QueryField} that can be used in the {@link QueryCondition}
     */
    public abstract List<QueryField> queryFields();

    /**
     * Returns the Entity that is represented in the search query / results.
     * This entity will also have declared the @{@link javax.persistence.SqlResultSetMapping}
     * which corresponds to the {@link SearchResultSet} R.
     */
    public abstract Class<E> getEntityClass();

    /**
     * This defines the actual pojo which is mapped to the results returned from the query
     */
    public abstract Class<R> getSearchResult();

    /**
     * The native query string which  declares the exact same columns name that are
     * declared in the {@link SearchableEntity}  @{@link javax.persistence.SqlResultSetMapping} declaration.
     */
    public abstract String nativeQuery();


    /**
     * Returns the  name of the POJO used in the  {@link javax.persistence.SqlResultSetMapping}
     */
    public abstract String resultSetMapping();

    /**
     * Returns the grouping clause which should match a table column definition
     * in the nativeQuery.
     */
    public abstract String groupByClause();

    /**
     * Build the native query using the {@link QueryCondition} field name and values.
     *
     * @return Native query String
     */
    public String toNativeQueryWithConditions() {

        StringBuilder queryBuilder = new StringBuilder(nativeQuery());


        if (whereCondition.test(queryConditions)) {

            queryBuilder.append(WHERE);
            addConditions(queryBuilder,
                    queryConditions.stream().filter(c -> !c.getField().isAggregated()));
        }

        //add grouping condition
        addGroupByConditions(queryBuilder);

        //build the where clause
        if (aggregateCondition.test(queryConditions)) {

            queryBuilder.append(HAVING);
            addConditions(queryBuilder,
                    queryConditions.stream().filter(c -> c.getField().isAggregated()));
        }

        //add ordering
        addOrderingCondition(queryBuilder);


        return queryBuilder.toString();
    }

    /**
     * Append the query ordering condition to the native query
     * @param queryBuilder
     */
    private void addOrderingCondition(StringBuilder queryBuilder) {

        if(Objects.nonNull(orderCondition)){
            queryBuilder.append(ORDER_BY)
                    .append(orderCondition.toNativeOrderClause());
        }


    }

    /**
     * Appends the aggregated query conditions column names to the GROUP BY conditions
     * which then will have a corresponding
     * @param queryBuilder
     */
    private void addGroupByConditions(StringBuilder queryBuilder) {


        if(!StringUtils.isEmpty(groupByClause())){

            queryBuilder.append(GROUP_BY);
            queryBuilder.append(groupByClause());
        }



    }

    /**
     * Append each {@link QueryCondition}s where we append their sql column name the operator symbol
     *
     * @param queryBuilder
     * @param queryConditions
     */
    private void addConditions(StringBuilder queryBuilder, Stream<QueryCondition> queryConditions) {
        int index = 0;

        for (QueryCondition condition : queryConditions.collect(Collectors.toList())) {

            if (index == 0) {
                queryBuilder.append(condition.toQueryClause());

            } else {
                queryBuilder.append(" ").append(conditionOperator.name()).append(" ").append(condition.toQueryClause());

            }
            index++;
        }

    }

    public OrderCondition getOrderCondition() {
        return orderCondition;
    }

    public void setOrderCondition(OrderCondition orderCondition) {
        this.orderCondition = orderCondition;
    }
    /**
     * Add new query condition to exist conditions.
     *
     * @param queryCondition
     */
    public void addCondition(final QueryCondition queryCondition) {

        if (!queryFields().contains(queryCondition.getField())) {
            throw new IllegalArgumentException(
                    String.format("Query condition field %n is not a valid field for %",
                            queryCondition.getField().getSqlColumnName(), getSearchResult().getSimpleName()
                    ));
        }


        this.queryConditions.add(queryCondition);
    }


}
