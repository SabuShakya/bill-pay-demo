package com.billpayment.billpaydemo.controller;

import com.billpayment.billpaydemo.dto.BillEnquiryRequestDTO;
import com.billpayment.billpaydemo.dto.BillEnquiryResponseDTO;
import com.billpayment.billpaydemo.dto.BillPaymentRequestDTO;
import com.billpayment.billpaydemo.dto.BillPaymentResponseDTO;
import com.billpayment.billpaydemo.service.BillEnquiryService;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.billpayment.billpaydemo.constants.ApiConstants.BASE;
import static com.billpayment.billpaydemo.constants.ApiConstants.BillApiConstants.FETCH_BILL_STATEMENT;
import static com.billpayment.billpaydemo.constants.ApiConstants.BillApiConstants.PAY_BILL;

@RestController
@RequestMapping(BASE)
@Api("Api Resources for Bill.")
public class BillEnquiryController {

    private final BillEnquiryService billEnquiryService;

    public BillEnquiryController(BillEnquiryService billEnquiryService) {
        this.billEnquiryService = billEnquiryService;
    }

    @PutMapping(FETCH_BILL_STATEMENT)
    public ResponseEntity<BillEnquiryResponseDTO> getBillStatement(@RequestBody BillEnquiryRequestDTO billEnquiryRequestDTO) {
        return ResponseEntity.ok(billEnquiryService.fetchBillStatement(billEnquiryRequestDTO));
    }

    @PostMapping(PAY_BILL)
    public ResponseEntity<BillPaymentResponseDTO> payBill(@RequestBody BillPaymentRequestDTO billPaymentRequestDTO) {
        return ResponseEntity.ok(billEnquiryService.payBill(billPaymentRequestDTO));
    }

}
