package com.billpayment.billpaydemo.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChitrawanTxnStatusRequestDTO {

    private String username;

    private Double amount;

    private String transactionId;
}
