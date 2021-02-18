package com.billpayment.billpaydemo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillPaymentRequestDTO {

    private String clientUsername;

    private String password;

    private String customerId;

    private String counter;

    private String requestId;

    private Double totalAmount;

    private Double advanceAmount;

    private Double paidAmount;

    private Double fine;

    private Double discount;

    private Integer monthFrom;

    private Integer monthTo;

    private Character isTotalBillPayment;

}
