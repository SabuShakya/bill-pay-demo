package com.billpayment.billpaydemo.service.impl;

import com.billpayment.billpaydemo.entity.ChitrawanRequestLog;
import com.billpayment.billpaydemo.repository.ChitrawanRequestLogRepository;
import com.billpayment.billpaydemo.service.ChitrawanRequestLogService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class ChitrawanRequestLogServiceImpl implements ChitrawanRequestLogService {

    private final ChitrawanRequestLogRepository chitrawanRequestLogRepository;


    @Override
    public void saveChitrawanRequestLog(ChitrawanRequestLog chitrawanRequestLog) {
        chitrawanRequestLogRepository.save(chitrawanRequestLog);
    }

    @Override
    public ChitrawanRequestLog findChitrawanRequestLogByRequestId(String requestId) {
        return chitrawanRequestLogRepository.findByRequestId(requestId);
    }

    @Override
    public Optional<ChitrawanRequestLog> findChitrawanRequestLogByTransactionIdAndAmountPaid(String transactionId, Double amount) {
        return chitrawanRequestLogRepository.findByTransactionIdAndAmountPaid(
                transactionId, amount
        );
    }
}
