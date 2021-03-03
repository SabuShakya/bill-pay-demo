package com.billpayment.billpaydemo.repository;

import com.billpayment.billpaydemo.entity.ChitrawanRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChitrawanRequestLogRepository extends JpaRepository<ChitrawanRequestLog, Long> {

    @Query("SELECT crl FROM ChitrawanRequestLog crl WHERE crl.requestId= :requestId")
    ChitrawanRequestLog findByRequestId(@Param("requestId") String requestId);

    @Query("SELECT crl FROM ChitrawanRequestLog crl WHERE crl.transactionId= :transactionId AND crl.amountPaid= :amount")
    Optional<ChitrawanRequestLog> findByTransactionIdAndAmountPaid(@Param("transactionId") String transactionId,
                                                                   @Param("amount") Double amount);
}
