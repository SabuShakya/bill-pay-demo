package com.billpayment.billpaydemo.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChitrawanPackages {

    private String package_id;

    private String package_name;

    private Double amount;

    private Boolean current;

}
