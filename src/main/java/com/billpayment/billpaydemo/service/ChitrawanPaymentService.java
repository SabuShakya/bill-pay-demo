package com.billpayment.billpaydemo.service;

import com.billpayment.billpaydemo.dto.ChitrawanPaymentActivateResponseDTO;
import com.billpayment.billpaydemo.dto.ChitrawanPaymentRequestDTO;
import com.billpayment.billpaydemo.dto.ChitrawanTxnStatusRequestDTO;

public interface ChitrawanPaymentService {
    ChitrawanPaymentActivateResponseDTO activatePayment(ChitrawanPaymentRequestDTO chitrawanPaymentRequestDTO);

    ChitrawanPaymentActivateResponseDTO checkTransactionStatus(ChitrawanTxnStatusRequestDTO chitrawanTxnStatusRequestDTO);
}
