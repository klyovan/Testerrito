package com.netcracker.testerritto.exceptions;

public class ApiRequestException extends RuntimeException {
    private final Throwable cause;
    private final String message;

    public ApiRequestException(String message) {
        super(message);
        this.message = message;
        this.cause = null;
    }

    public ApiRequestException(String message, Throwable cause) {
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
