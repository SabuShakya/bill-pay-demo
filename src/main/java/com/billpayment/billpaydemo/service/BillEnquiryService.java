package com.billpayment.billpaydemo.service;

import com.billpayment.billpaydemo.dto.BillEnquiryRequestDTO;
import com.billpayment.billpaydemo.dto.BillEnquiryResponseDTO;
import com.billpayment.billpaydemo.dto.BillPaymentRequestDTO;
import com.billpayment.billpaydemo.dto.BillPaymentResponseDTO;

public interface BillEnquiryService {

    public BillEnquiryResponseDTO fetchBillStatement(BillEnquiryRequestDTO billEnquiryRequestDTO);

    public BillPaymentResponseDTO payBill(BillPaymentRequestDTO billPaymentRequestDTO);
}
