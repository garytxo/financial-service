package com.murray.financial.domain.repository.query;

import com.murray.financial.domain.entity.AccountTransfer;
import com.murray.financial.querybuilder.QueryField;
import com.murray.financial.querybuilder.SearchQuery;

import java.util.Arrays;
import java.util.List;

import static com.murray.financial.domain.repository.query.TransferQueryField.*;

/**
 * {@inheritDoc}
 */
public class TransferSearch extends SearchQuery<AccountTransfer, TransferAccountResult> {


    public TransferSearch() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<QueryField> queryFields() {
        return Arrays.asList(SOURCE, DESTINATION, STATUS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<AccountTransfer> getEntityClass() {
        return AccountTransfer.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<TransferAccountResult> getSearchResult() {
        return TransferAccountResult.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String nativeQuery() {
        return TransferQueryField.NATIVE_QUERY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String groupByClause() {
        return GROUP_BY_TRANSFER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String resultSetMapping() {
        return TransferAccountResult.TRANSFER_ACCOUNT_RESULTS;
    }
}
