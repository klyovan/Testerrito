package com.netcracker.testerritto.exceptions;


public class ServiceException extends RuntimeException {
    private final Throwable cause;
    private final String message;

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.cause = cause;
        this.message = message;
    }

    public Throwable getCause() {
        return cause;
    }

    public String getMessage() {
        return message;
    }
}
