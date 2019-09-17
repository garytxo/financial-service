package com.murray.financial.contollers.advice;

import com.murray.financial.exceptions.AccountCreationException;
import com.murray.financial.exceptions.NotFoundException;
import com.murray.financial.exceptions.TransferCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * This handler will catch all the exception thrown by the REST or Controller layer and convert them to an HTTP code and message.
 */
@ControllerAdvice
public class FinancialRestExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FinancialRestExceptionHandler.class);


    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<ErrorResponse> notFound(NotFoundException ex, WebRequest webRequest) {

        LOGGER.error(String.format("Not found %s Exception", ex.getClass().getSimpleName()), ex);

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), ResponseErrorType.NOT_ACCEPTABLE, ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<ErrorResponse> handleConflict(IllegalArgumentException ex, WebRequest webRequest) {

        LOGGER.error(String.format("%s Exception", ex.getClass().getSimpleName()), ex);

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), ResponseErrorType.NOT_ACCEPTABLE, ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = {AccountCreationException.class})
    protected ResponseEntity<ErrorResponse> accountCreatonErrors(AccountCreationException ex, WebRequest webRequest) {

        LOGGER.error(String.format("Account creation error %s Exception", ex.getClass().getSimpleName()), ex);

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), ResponseErrorType.ACCOUNT_ERROR, ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);

    }

    @ExceptionHandler(value = {TransferCreationException.class})
    protected ResponseEntity<ErrorResponse> tranferCreationError(TransferCreationException ex, WebRequest webRequest) {

        LOGGER.error(String.format("Transfer creation error %s Exception", ex.getClass().getSimpleName()), ex);

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), ResponseErrorType.ACCOUNT_ERROR, ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);

    }
}
