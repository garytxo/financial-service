package com.murray.financial.domain.repository.Impl;

import com.murray.financial.domain.repository.AccountTransferJPACustomRepository;
import com.murray.financial.domain.repository.query.TransferAccountResult;
import com.murray.financial.domain.repository.query.TransferSearch;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

public class AccountTransferJPACustomRepositoryImpl
        extends JPACustomRepositoryBase<TransferAccountResult, TransferSearch>
        implements AccountTransferJPACustomRepository<TransferAccountResult, TransferSearch> {

    public AccountTransferJPACustomRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<TransferAccountResult> searchBy(TransferSearch searchQuery) {
        Query query = buildNativeQueryWith(searchQuery);

        System.out.println("+++++++++++++++++");
        System.out.println(searchQuery.toNativeQueryWithConditions());
        System.out.println("+++++++++++++++++");

        searchQuery.queryConditions().forEach(

                qc -> {
                    System.out.println("****cfFirld:"+qc.getField().getSqlColumnName() + " value:"+qc.getValue());
                    //     query.setParameter(qc.getField().getSqlColumnName(), qc.getValue())
                }

        );

        searchQuery.queryConditions().forEach(qc -> query.setParameter(qc.getField().getSqlColumnName(), qc.getValue()));

        return query.getResultList();
    }
}
