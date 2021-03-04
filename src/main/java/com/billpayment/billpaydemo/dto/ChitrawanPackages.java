package com.billpayment.billpaydemo.dto;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ChitrawanPackages implements Serializable {

    private String package_id;

    private String package_name;

    private Double amount;

    private Boolean current;

}
