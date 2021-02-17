package com.billpayment.billpaydemo.service;

import com.billpayment.billpaydemo.dto.BillEnquiryRequestDTO;
import com.billpayment.billpaydemo.dto.BillEnquiryResponseDTO;

public interface BillEnquiryService {

    public BillEnquiryResponseDTO fetchBillStatement(BillEnquiryRequestDTO billEnquiryRequestDTO);
}
