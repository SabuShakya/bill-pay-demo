package com.billpayment.billpaydemo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BillPaymentResponseDTO {

    private String responseCode;

    private String messgae;

    private String requestId;
}
