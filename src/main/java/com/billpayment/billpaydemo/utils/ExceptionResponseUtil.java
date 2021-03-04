package com.billpayment.billpaydemo.utils;

import com.billpayment.billpaydemo.dto.GenericErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ExceptionResponseUtil {

    public  static String getExceptionMessageForMethodArgumentNotValid(MethodArgumentNotValidException ex){
        List<String> violations = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> (fieldError.getField() + ": " + fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        return String.join(", ", violations);

    }

    public static GenericErrorResponse buildGenericErrorResponse(String message, HttpStatus status) {
        return GenericErrorResponse.builder()
                .message(message)
                .responseStatus(status)
                .timeStamp(new Date())
                .build();
    }
}
