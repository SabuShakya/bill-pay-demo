package com.billpayment.billpaydemo.repository;

import com.billpayment.billpaydemo.entity.TokenDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenDetailsRepository extends JpaRepository<TokenDetails, Long> {

    @Query("SELECT td FROM TokenDetails td WHERE td.status= :status")
    TokenDetails findByStatus(@Param("status") Character status);
}
