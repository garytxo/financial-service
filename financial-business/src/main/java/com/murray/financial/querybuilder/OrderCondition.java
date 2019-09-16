package com.murray.financial.querybuilder;

/**
 * A ordering condition to
 */
public class OrderCondition {


    private OrderBy orderBy;

    private QueryField field;

    public OrderCondition(OrderBy orderBy, QueryField field) {
        this.orderBy = orderBy;
        this.field = field;
    }

    public OrderCondition(QueryField field) {
        this.orderBy = OrderBy.DESC;
        this.field = field;
    }

    public void setOrderBy(OrderBy orderBy) {
        this.orderBy = orderBy;
    }

    public String toNativeOrderClause(){

        return field.getSqlColumnName().concat(" ").concat(orderBy.getSign());
    }

}
