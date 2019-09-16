package com.murray.financial.domain.enums;

import org.springframework.util.StringUtils;

/**
 * Represents the actual bank account getStatus
 */
public enum AccountStatus {

    /* Active account */
    ACTIVE,
    /* Inactive account */
    DISABLED,
    /* Deleted account */
    DELETED;

    /**
     * Converts the status to its corresponding {@link AccountStatus}
     * @param status String status
     * @return {@link AccountStatus}
     * @throws  IllegalArgumentException when status is invalid
     */
    public static AccountStatus toStatus(String status){

        if(StringUtils.isEmpty(status)){
            throw  new IllegalArgumentException("Status field is required");
        }

        try{

            return AccountStatus.valueOf(status);

        }catch (NullPointerException np){

            throw  new IllegalArgumentException(String.format("Status %s is not supported",status));
        }
    }
}
