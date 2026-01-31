package com.flipfit.exceptions;

public class DbConnectionException extends Exception {
    public DbConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
