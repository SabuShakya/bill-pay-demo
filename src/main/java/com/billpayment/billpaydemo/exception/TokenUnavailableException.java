package com.billpayment.billpaydemo.exception;

public class TokenUnavailableException extends RuntimeException {
    public TokenUnavailableException(String message) {
        super(message);
    }
}
