package com.billpayment.billpaydemo.exception;

public class DuplicatedRequestIdException extends RuntimeException {
    public DuplicatedRequestIdException(String message) {
        super(message);
    }
}
