package com.murray.financial.domain.repository.query;

import com.murray.financial.querybuilder.SearchResultSet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents an {@link com.murray.financial.domain.entity.AccountTransfer} which is returned from the
 * {@link com.murray.financial.domain.repository.Impl.AccountTransferJPACustomRepositoryImpl}
 */
public class TransferAccountResult implements Serializable, SearchResultSet {


    public static final String TRANSFER_ACCOUNT_RESULTS = "TransferAccountResults";

    /**
     * internal unique id
     */
    private Long transferId;

    /**
     * time stamp when transfer occurred
     */
    private LocalDateTime transferSent;

    /**
     * transfer source  bank account IBAN number
     */
    private String srcIbanNumber;

    /**
     * destination transfer bank account IBAN number
     */
    private String destIbanNumber;

    /**
     * transfer getAmount
     */
    private BigDecimal amount;

    public TransferAccountResult(Long transferId, LocalDateTime transferSent,
                                 String srcIbanNumber, String destIbanNumber, BigDecimal amount) {
        this.transferId = transferId;
        this.transferSent = transferSent;
        this.srcIbanNumber = srcIbanNumber;
        this.destIbanNumber = destIbanNumber;
        this.amount = amount;
    }

    @Override
    public String resultSetObjectName() {
        return TRANSFER_ACCOUNT_RESULTS;
    }


    public Long getTransferId() {
        return transferId;
    }

    public LocalDateTime getTransferSent() {
        return transferSent;
    }

    public String getSourceAccountIbanNumber() {
        return srcIbanNumber;
    }

    public String getDestIbanAccountIbanNumber() {
        return destIbanNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
