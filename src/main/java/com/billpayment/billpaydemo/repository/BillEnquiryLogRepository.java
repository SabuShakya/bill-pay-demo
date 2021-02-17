package com.billpayment.billpaydemo.repository;

import com.billpayment.billpaydemo.entity.BillEnquiryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillEnquiryLogRepository extends JpaRepository<BillEnquiryLog, Long> {

}
