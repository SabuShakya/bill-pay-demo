package com.billpayment.billpaydemo.controller;

import com.billpayment.billpaydemo.dto.ChitrawanPaymentRequestDTO;
import com.billpayment.billpaydemo.dto.ChitrawanTxnStatusRequestDTO;
import com.billpayment.billpaydemo.dto.ChitrawanUserDetailsRequestDTO;
import com.billpayment.billpaydemo.service.ChitrawanPaymentService;
import com.billpayment.billpaydemo.service.ChitrawanUserDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.billpayment.billpaydemo.constants.ApiConstants.BASE;
import static com.billpayment.billpaydemo.constants.ApiConstants.ChitrawanApiConstants.*;

@RestController
@RequestMapping(BASE)
public class ChitrawanController {

    private final ChitrawanUserDetailService userDetailService;
    private final ChitrawanPaymentService chitrawanPaymentService;

    public ChitrawanController(ChitrawanUserDetailService userDetailService, ChitrawanPaymentService chitrawanPaymentService) {
        this.userDetailService = userDetailService;
        this.chitrawanPaymentService = chitrawanPaymentService;
    }

    @PostMapping(GET_USER_DETAILS)
    public ResponseEntity<?> fetchUserDetails(@RequestBody ChitrawanUserDetailsRequestDTO userDetailsRequestDTO) {
        return ResponseEntity.ok(userDetailService.fetchUserDetails(userDetailsRequestDTO));
    }

    @PostMapping(CHITRAWAN_BILL_PAY)
    public ResponseEntity<?> paymentActivate(@Valid @RequestBody ChitrawanPaymentRequestDTO chitrawanPaymentRequestDTO) {
        return ResponseEntity.ok(chitrawanPaymentService.activatePayment(chitrawanPaymentRequestDTO));
    }

    @PostMapping(CHITRAWAN_CHECK_TRANSACTION_STATUS)
    public ResponseEntity<?> checkStatus(@Valid @RequestBody ChitrawanTxnStatusRequestDTO chitrawanTxnStatusRequestDTO) {
        return ResponseEntity.ok(chitrawanPaymentService.checkTransactionStatus(chitrawanTxnStatusRequestDTO));
    }

}
