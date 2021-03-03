package com.billpayment.billpaydemo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ChitrawanUserDetailResponseDTO {

    private String userId;

    private String name;

    private String mobileNo;

    private String accountStatus;

    private String plan;

    private String expiryDate;

    private String planStatus;

    private String requestId;

    private Double payableAmount;

    private List<PackageResponseDTO> packages;
}
