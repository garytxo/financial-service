package com.murray.financial.querybuilder;

public interface SearchResultSet {

    /**
     * The name given to the result set mapping class which is used
     * for mapping the results to the search result columns defined
     * in the {@link javax.persistence.SqlResultSetMapping}
     */
    String resultSetObjectName();
}
