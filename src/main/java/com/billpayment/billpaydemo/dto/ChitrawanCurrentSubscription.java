package com.billpayment.billpaydemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ChitrawanCurrentSubscription implements Serializable {

    @JsonProperty("package")
    private String Package;

    private String end_date;

    private String status;

}
