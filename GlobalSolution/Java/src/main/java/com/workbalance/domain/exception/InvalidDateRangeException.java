package com.workbalance.domain.exception;

/**
 * Exception thrown when date range validation fails
 */
public class InvalidDateRangeException extends BusinessException {
    
    public InvalidDateRangeException(String message) {
        super("INVALID_DATE_RANGE", message);
    }
}
