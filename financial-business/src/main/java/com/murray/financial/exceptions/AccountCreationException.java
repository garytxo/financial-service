package com.murray.financial.exceptions;

/**
 * Exception thrown if there is any issue once creating a {@link com.murray.financial.domain.entity.BankAccount}
 */
public class AccountCreationException extends RuntimeException {

    public AccountCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountCreationException(String message) {
        super(message);
    }
}
