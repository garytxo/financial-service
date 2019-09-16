package com.murray.financial.contollers.advice;

import java.time.LocalDateTime;

public class ErrorResponse {

    private int errorCode;
    private LocalDateTime when;
    private String fieldName;
    private ResponseErrorType errorType;
    private String errorMessage;


    ErrorResponse(int errorCode, ResponseErrorType errorType, String errorMessage) {
        this.when = LocalDateTime.now();
        this.fieldName = "";
        this.errorType = errorType;
        this.errorMessage = errorMessage;
    }

    ErrorResponse(int errorCode, String fieldName, ResponseErrorType errorType, String errorMessage) {
        this.when = LocalDateTime.now();
        this.errorCode = errorCode;
        this.fieldName = fieldName;
        this.errorType = errorType;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
