package com.billpayment.billpaydemo.repository;

import com.billpayment.billpaydemo.entity.BillEnquiryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BillEnquiryLogRepository extends JpaRepository<BillEnquiryLog, Long> {

    @Query("SELECT bel from BillEnquiryLog  bel WHERE bel.requestId= :requestId")
    BillEnquiryLog findBillEnquiryLogByRequestId(@Param("requestId") String requestId);
}
