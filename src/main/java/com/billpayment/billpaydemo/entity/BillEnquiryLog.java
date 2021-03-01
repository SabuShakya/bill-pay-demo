package com.billpayment.billpaydemo.entity;

import com.billpayment.billpaydemo.auditable.Auditable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bill_enquiry_log")
@Getter
@Setter
public class BillEnquiryLog extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "account_number")
    private Integer accountNumber;

    @Column(name = "requested_by")
    private String requestedBy;

    @Column(name = "requested_date")
    private Date requestedDate;

    @Column(name = "status")
    private String status;
//    ENQUIRY SUCCESS/FAILURE
//    PAYMENT SUCCESS?FAILURE

    @Column(name = "transaction_number")
    private Long transactionNumber; // sn

    // should be unique for every enquiry, and requestId should exist for payment.
    @Column(name = "requestId")
    private String requestId;


}
