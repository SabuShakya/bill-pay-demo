package com.billpayment.billpaydemo.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PackageResponseDTO {

    private String packageId;

    private String packageName;

    private Double rate;

    private boolean selected;
}
