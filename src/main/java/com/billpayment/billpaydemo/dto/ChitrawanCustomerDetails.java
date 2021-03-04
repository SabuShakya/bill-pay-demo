package com.billpayment.billpaydemo.dto;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ChitrawanCustomerDetails implements Serializable {

    private String username;

    private String name;

    private String mobile;

    private String status;
}
