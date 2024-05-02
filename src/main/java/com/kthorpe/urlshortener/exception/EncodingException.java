package com.kthorpe.urlshortener.exception;

public class EncodingException extends Exception {
    private String message;

    public EncodingException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
