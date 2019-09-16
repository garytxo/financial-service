package com.murray.financial.exceptions;

/**
 * thrown during the transfer creation or execution period when there are invalid
 * transfer state, such as invalid account .
 */
public class TransferCreationException extends RuntimeException {

    public TransferCreationException() {
    }

    public TransferCreationException(Throwable cause) {
        super(cause);
    }

    public TransferCreationException(String message) {
        super(message);
    }
}
