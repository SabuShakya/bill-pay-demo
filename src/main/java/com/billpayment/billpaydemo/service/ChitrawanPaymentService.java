package com.billpayment.billpaydemo.service;

import com.billpayment.billpaydemo.dto.ChitrawanPaymentActivateResponseDTO;
import com.billpayment.billpaydemo.dto.ChitrawanPaymentRequestDTO;

public interface ChitrawanPaymentService {
    ChitrawanPaymentActivateResponseDTO activatePayment(ChitrawanPaymentRequestDTO chitrawanPaymentRequestDTO);
}
