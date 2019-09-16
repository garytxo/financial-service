package com.murray.financial.domain.repository.Impl;

import com.murray.financial.domain.repository.BackAccountJPARepository;
import com.murray.financial.domain.repository.BankAccountJPACustomRepository;
import com.murray.financial.domain.repository.query.BankAccountResult;
import com.murray.financial.domain.repository.query.BankAccountSearch;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * {@inheritDoc}
 */
public class BankAccountJPACustomRepositoryImpl
        extends JPACustomRepositoryBase<BankAccountResult, BankAccountSearch>
        implements BankAccountJPACustomRepository<BankAccountResult, BankAccountSearch> {


    public BankAccountJPACustomRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BankAccountResult> searchBy(BankAccountSearch searchQuery) {

        Query query = buildNativeQueryWith(searchQuery);

        System.out.println("**************  AAAA ************************************");

        System.out.println("********>"+searchQuery.toNativeQueryWithConditions());


        System.out.println("****************** BBB ********************************");

        searchQuery.queryConditions().forEach(qc -> query.setParameter(qc.getField().getSqlColumnName(), qc.getValue()));

        return query.getResultList();
    }


}
