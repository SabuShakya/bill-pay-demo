package com.billpayment.billpaydemo.dto;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ChitrawanDueResponse implements Serializable {

    private Double amount;

    private String message;

}
