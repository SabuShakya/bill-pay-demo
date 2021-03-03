package com.billpayment.billpaydemo.repository;

import com.billpayment.billpaydemo.entity.ChitrawanRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChitrawanRequestLogRepository extends JpaRepository<ChitrawanRequestLog, Long> {

    @Query("SELECT crl FROM ChitrawanRequestLog crl WHERE crl.requestId= :requestId")
    ChitrawanRequestLog findByRequestId(String requestId);

    @Query("SELECT crl FROM ChitrawanRequestLog crl WHERE crl.transactionId= :transactionId AND crl.amountPaid= :amount")
    Optional<ChitrawanRequestLog> findByTransactionIdAndAmountPaid(String transactionId, Double amount);
}
