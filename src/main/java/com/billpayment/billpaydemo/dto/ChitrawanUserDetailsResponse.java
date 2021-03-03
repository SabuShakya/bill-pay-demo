package com.billpayment.billpaydemo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChitrawanUserDetailsResponse {

    private boolean status;

    private String code;

    private ChitrawanResponseDetails details;

    private String message;
}
