package com.murray.financial.domain.repository.query;

import com.murray.financial.domain.entity.BankAccount;
import com.murray.financial.querybuilder.QueryField;
import com.murray.financial.querybuilder.SearchQuery;

import java.util.Arrays;
import java.util.List;

import static com.murray.financial.domain.repository.query.BankAccountQueryField.*;

/**
 * {@inheritDoc}
 */
public class BankAccountSearch extends SearchQuery<BankAccount, BankAccountResult> {


    public BankAccountSearch() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<BankAccount> getEntityClass() {
        return BankAccount.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<BankAccountResult> getSearchResult() {
        return BankAccountResult.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String nativeQuery() {
        return NATIVE_QUERY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String groupByClause() {
        return GROUP_BY_ACCOUNT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<QueryField> queryFields() {

        return Arrays.asList(IBAN_NUMBER, BALANCE, STATUS, CURRENCY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String resultSetMapping() {
        return BankAccountResult.BANK_ACCOUNT_RESULTS;
    }
}
