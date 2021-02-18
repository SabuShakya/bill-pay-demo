package com.billpayment.billpaydemo.constants;

public interface CommonConstants {

    String AUTHORIZATION_HEADER = "Authorization";
    String AUTHORIZATION_HEADER_PREFIX = "Bearer ";

    interface BillEnquiryStatus {
        String ENQUIRY_SUCCESS = "ENQUIRY_SUCCESS";
        String ENQUIRY_FAILURE = "ENQUIRY_FAILURE";
        String PAYMENT_SUCCESS = "PAYMENT_SUCCESS";
        String PAYMENT_FAILURE = "PAYMENT_FAILURE";
    }
}
