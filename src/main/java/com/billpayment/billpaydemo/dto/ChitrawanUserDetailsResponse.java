package com.billpayment.billpaydemo.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ChitrawanUserDetailsResponse  implements Serializable {

    private boolean status;

    private String code;

    private ChitrawanResponseDetails details;

    private String message;
}
