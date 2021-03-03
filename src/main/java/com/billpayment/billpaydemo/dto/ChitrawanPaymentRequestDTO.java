package com.billpayment.billpaydemo.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChitrawanPaymentRequestDTO {

    private String username;

    private String packageId;

    private Double amount;

    private String transactionId;

    private String requestId;
}
