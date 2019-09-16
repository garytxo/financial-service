package com.murray.financial.querybuilder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * All the operators that can be used to compare a {@link QueryField} with a value in a {@link QueryCondition}.
 */
public enum Operator {


    EQUALS("=") {
        @Override
        public final Predicate buildPredicate(final CriteriaBuilder criteriaBuilder, final Expression<Path> path,
                                              final Object value) {
            return criteriaBuilder.equal(path, value);
        }
    },
    NOT_EQUALS("!=") {
        @Override
        public final Predicate buildPredicate(final CriteriaBuilder criteriaBuilder, final Expression<Path> path,
                                              final Object value) {
            return criteriaBuilder.notEqual(path, value);
        }
    };

    /*
    GREATER_THAN(">"),
    GREATER_OR_EQUAL(">="),
    LOWER_THAN("<"),
    LOWER_OR_EQUAL("<="),
    IN("in");*/


    private String symbol;


    Operator(String symbol) {
        this.symbol = symbol;
    }


    public String getSymbol() {
        return symbol;
    }

    public abstract Predicate buildPredicate(final CriteriaBuilder criteriaBuilder, final Expression<Path> path,
                                             final Object value);


}

