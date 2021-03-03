package com.billpayment.billpaydemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChitrawanCurrentSubscription {

    @JsonProperty("package")
    private String Package;

    private String end_date;

    private String status;

}
