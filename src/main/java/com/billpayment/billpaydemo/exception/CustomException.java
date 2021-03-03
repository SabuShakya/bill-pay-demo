package com.billpayment.billpaydemo.exception;

import com.billpayment.billpaydemo.dto.GenericErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
public class CustomException extends RuntimeException {

    private GenericErrorResponse errorResponse;
    public CustomException(String message, HttpStatus httpStatus) {
        super(message);
        setErrorResponse(message, httpStatus);
    }

    private void setErrorResponse(String message, HttpStatus httpStatus) {
        errorResponse = GenericErrorResponse.builder()
                .message(message)
                .responseStatus(httpStatus)
                .timeStamp(new Date())
                .build();
    }

}
