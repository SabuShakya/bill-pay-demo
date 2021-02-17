package com.billpayment.billpaydemo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillEnquiryRequestDTO {

    private String mobileNumber;

    private Long customerId;

    private String requestId;

    private String clientUsername;

    private String password;
}
