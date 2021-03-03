package com.billpayment.billpaydemo.service.impl;

import com.billpayment.billpaydemo.entity.ChitrawanRequestLog;
import com.billpayment.billpaydemo.repository.ChitrawanRequestLogRepository;
import com.billpayment.billpaydemo.service.ChitrawanRequestLogService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class ChitrawanRequestLogServiceImpl implements ChitrawanRequestLogService {

    private final ChitrawanRequestLogRepository chitrawanRequestLogRepository;


    @Override
    public void saveChitrawanRequestLog(ChitrawanRequestLog chitrawanRequestLog) {
        chitrawanRequestLogRepository.save(chitrawanRequestLog);
    }
}
