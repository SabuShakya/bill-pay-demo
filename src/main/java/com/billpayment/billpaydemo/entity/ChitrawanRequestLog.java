package com.billpayment.billpaydemo.entity;

import com.billpayment.billpaydemo.auditable.Auditable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "chitrawan_request_log")
@Getter
@Setter
public class ChitrawanRequestLog extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "requestId")
    private String requestId;

    @Column(name = "status")
    private String status;

    @Column(name = "customer_username")
    private String customerUsername;

    @Column(name = "customer_mobile")
    private String customerMobile;

    @Column(name = "due_amount")
    private Double dueAmount;

    @Column(name = "amount_paid")
    private Double amountPaid;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "reference_code")
    private String referenceCode;

}
