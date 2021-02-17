package com.billpayment.billpaydemo.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BillStatementMonthlyDetails {

    private String date;

    private String cons;

    private Double amount;

    private Double rebate;

    private Double total;

    private Long sn;
}
