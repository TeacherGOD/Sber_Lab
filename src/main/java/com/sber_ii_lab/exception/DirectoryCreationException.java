package com.sber_ii_lab.exception;

public class DirectoryCreationException extends RuntimeException {
    public DirectoryCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}