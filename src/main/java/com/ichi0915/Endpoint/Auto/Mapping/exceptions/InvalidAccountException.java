package com.ichi0915.Endpoint.Auto.Mapping.exceptions;

public class InvalidAccountException extends IllegalArgumentException {
    public InvalidAccountException(String message) {
        super(message);
    }

    public InvalidAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
