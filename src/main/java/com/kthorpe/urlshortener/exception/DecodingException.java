package com.kthorpe.urlshortener.exception;

public class DecodingException extends Exception {
    private String message;

    public DecodingException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
