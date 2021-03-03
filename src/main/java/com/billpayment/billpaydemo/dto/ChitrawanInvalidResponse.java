package com.billpayment.billpaydemo.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChitrawanInvalidResponse {

    private Boolean status;

    private String code;

    private String message;
}
