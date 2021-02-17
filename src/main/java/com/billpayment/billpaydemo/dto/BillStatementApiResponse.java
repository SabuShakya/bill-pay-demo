package com.billpayment.billpaydemo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillStatementApiResponse {

    private BillStatementDetail detail;

    private String message;

    private String responseCode;

    private Integer accountNo;

    private Long sn;
}
