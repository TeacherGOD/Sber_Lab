package com.sber_ii_lab.exception;

public class FileDeleteException extends RuntimeException{
    public FileDeleteException(String message, Throwable cause){
        super(message,cause);
    }
}
