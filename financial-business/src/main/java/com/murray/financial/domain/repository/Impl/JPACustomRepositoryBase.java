package com.murray.financial.domain.repository.Impl;

import com.murray.financial.domain.repository.BankAccountJPACustomRepository;
import com.murray.financial.querybuilder.SearchQuery;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Abstract clas that defined
 *
 * @param <R> Result getType that is resultset are mapped to
 * @param <Q> sub class of SearchQuery that contains the query conditions
 */
abstract class JPACustomRepositoryBase<R, Q extends SearchQuery>  {


    private EntityManager entityManager;

    JPACustomRepositoryBase(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Builds a native query using the defined {@link com.murray.financial.querybuilder.QueryCondition}
     * and {@link SearchQuery#nativeQuery()} instance defined in the {@link SearchQuery}.
     *
     * @param searchQuery {@link SearchQuery}
     * @return {@link Query}
     */
    Query buildNativeQueryWith(final SearchQuery searchQuery) {


        return entityManager.createNativeQuery(
                searchQuery.toNativeQueryWithConditions()
                , searchQuery.resultSetMapping()
        );
    }
}
