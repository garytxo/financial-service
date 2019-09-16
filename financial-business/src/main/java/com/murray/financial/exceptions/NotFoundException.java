package com.murray.financial.exceptions;

/**
 * Thrown when a transfer or account is not found
 */
public class NotFoundException extends RuntimeException{

    /**
     * The unqique id  was used to find the item
     */
    private String id;

    public NotFoundException(String message, String id) {
        super(message);
        this.id = id;
    }

    public NotFoundException(String message, Throwable cause, String id) {
        super(message, cause);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
