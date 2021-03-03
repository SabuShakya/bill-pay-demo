package com.billpayment.billpaydemo.controller;

import com.billpayment.billpaydemo.dto.ChitrawanPaymentRequestDTO;
import com.billpayment.billpaydemo.dto.ChitrawanUserDetailsRequestDTO;
import com.billpayment.billpaydemo.service.ChitrawanPaymentService;
import com.billpayment.billpaydemo.service.ChitrawanUserDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.billpayment.billpaydemo.constants.ApiConstants.BASE;
import static com.billpayment.billpaydemo.constants.ApiConstants.ChitrawanApiConstants.CHITRAWAN_BILL_PAY;
import static com.billpayment.billpaydemo.constants.ApiConstants.ChitrawanApiConstants.GET_USER_DETAILS;

@RestController
@RequestMapping(BASE)
public class ChitrawanUserController {

    private final ChitrawanUserDetailService userDetailService;
    private final ChitrawanPaymentService chitrawanPaymentService;

    public ChitrawanUserController(ChitrawanUserDetailService userDetailService, ChitrawanPaymentService chitrawanPaymentService) {
        this.userDetailService = userDetailService;
        this.chitrawanPaymentService = chitrawanPaymentService;
    }

    @PostMapping(GET_USER_DETAILS)
    public ResponseEntity<?> fetchUserDetails(@RequestBody ChitrawanUserDetailsRequestDTO userDetailsRequestDTO) {
        return ResponseEntity.ok(userDetailService.fetchUserDetails(userDetailsRequestDTO));
    }

    @PostMapping(CHITRAWAN_BILL_PAY)
    public ResponseEntity<?> paymentActivate(@RequestBody ChitrawanPaymentRequestDTO chitrawanPaymentRequestDTO) {
        return ResponseEntity.ok(chitrawanPaymentService.activatePayment(chitrawanPaymentRequestDTO));
    }

}
