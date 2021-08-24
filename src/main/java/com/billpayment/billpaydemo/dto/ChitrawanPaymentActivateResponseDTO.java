package com.billpayment.billpaydemo.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChitrawanPaymentActivateResponseDTO {

    private boolean status;

    private String code;

    private String message;

    private String reference_code;

}
