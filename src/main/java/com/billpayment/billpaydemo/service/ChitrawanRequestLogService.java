package com.billpayment.billpaydemo.service;

import com.billpayment.billpaydemo.entity.ChitrawanRequestLog;

import java.util.Optional;

public interface ChitrawanRequestLogService {

    void saveChitrawanRequestLog(ChitrawanRequestLog chitrawanRequestLog);

    ChitrawanRequestLog findChitrawanRequestLogByRequestId(String requestId);

    Optional<ChitrawanRequestLog> findChitrawanRequestLogByTransactionIdAndAmountPaid(String transactionId, Double amount);
}
