package com.billpayment.billpaydemo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BillStatementDetail {

    private String customerName;

    private String meterType;

    private String meterNumber;

    private Double advanceAvaliable;

    private Double amountPayable;

    private Double totalBillAmount;

    private Double totalFine;

    private Double totalDiscount;

    private List<BillStatementMonthlyDetails> monthlyDetails;

}
