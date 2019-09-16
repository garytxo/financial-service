package com.murray.financial.domain.repository;

import com.murray.financial.domain.repository.query.BankAccountResult;
import com.murray.financial.domain.repository.query.BankAccountSearch;
import com.murray.financial.querybuilder.QueryCondition;

import java.util.List;

/**
 * Bank account customized fragment interface that
 * declare JPA repository queries that extend the base repository
 * <p>
 * Bank account customized JPA repository fragment interface defines queries
 * that are then extended to the {@link BackAccountJPARepository}
 */
public interface BankAccountJPACustomRepository<T, Q> {


    /**
     * Find {@link BankAccountResult}s using the specific {@link QueryCondition}s ,
     * and OrderBy conditions that are defined by the
     *
     * @param searchQuery {@link BankAccountSearch} which is used to generate the native query
     * @return list of {@link BankAccountResult}
     */
    List<T> searchBy(final Q searchQuery);

}
