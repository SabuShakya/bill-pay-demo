package com.billpayment.billpaydemo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class BillEnquiryResponseDTO {

    private String resultCode;

    private String resultDescription;

    private String customerName;

    private Long customerCode;

    private String meterType;

    private String meterNumber;

    private Double advanceAvailable;

    private String requestId;

    private List<BillStatementMonthlyDetails> monthlyDetails;

    private Double amountPayable;

    private Double totalBillAmount;

    private Double totalFine;

    private Double totalDiscount;

    private Integer accountNo;

    private Long sn;

}
