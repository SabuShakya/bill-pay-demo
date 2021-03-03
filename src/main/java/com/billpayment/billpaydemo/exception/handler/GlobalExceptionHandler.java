package com.billpayment.billpaydemo.exception.handler;

import com.billpayment.billpaydemo.dto.GenericErrorResponse;
import com.billpayment.billpaydemo.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = UnAuthorizedException.class)
    public ResponseEntity<Object> handleUnAuthorizedException(UnAuthorizedException exception) {
        GenericErrorResponse errorResponse = buildGenericErrorResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(value = TokenUnavailableException.class)
    public ResponseEntity<Object> handleTokenUnavailableException(TokenUnavailableException exception) {
        GenericErrorResponse errorResponse = buildGenericErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<?> handleAllExceptions(Exception ex, WebRequest request) {
        GenericErrorResponse errorResponse = buildGenericErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PaymentException.class)
    public final ResponseEntity<?> handlePaymentException(PaymentException ex) {
        GenericErrorResponse errorResponse = buildGenericErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataDuplicateException.class)
    public final ResponseEntity<?> handleDuplicateRequestIdException(DataDuplicateException ex) {
        GenericErrorResponse errorResponse = buildGenericErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public final ResponseEntity<?> handleCustomException(CustomException ex) {
        return new ResponseEntity<>(ex.getErrorResponse(), ex.getErrorResponse().getResponseStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<?> handleRuntimeException(Exception ex) {
        GenericErrorResponse errorResponse = buildGenericErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private GenericErrorResponse buildGenericErrorResponse(String message, HttpStatus status) {
        return GenericErrorResponse.builder()
                .message(message)
                .responseStatus(status)
                .timeStamp(new Date())
                .build();
    }
}
