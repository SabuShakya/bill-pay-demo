package com.billpayment.billpaydemo.constants;

public interface CommonConstants {

    // TODO get from yml file
    String USERNAME = "nmbbank";
    String PASSWORD = "TESTTEST2323";
    String GRANT_TYPE = "password";
    String BASIC_AUTH_HEADER = "YzFpM250SUQ6UzNjcmU3";


    String AUTHORIZATION_HEADER = "Authorization";
    String AUTHORIZATION_HEADER_PREFIX = "Bearer ";

    interface BillEnquiryStatus {
        String ENQUIRY_SUCCESS = "ENQUIRY_SUCCESS";
        String ENQUIRY_FAILURE = "ENQUIRY_FAILURE";
        String PAYMENT_SUCCESS = "PAYMENT_SUCCESS";
        String PAYMENT_FAILURE = "PAYMENT_FAILURE";
    }
}
