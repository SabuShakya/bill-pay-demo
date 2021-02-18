package com.billpayment.billpaydemo.service.impl;

import com.billpayment.billpaydemo.configuration.proprties.KhanepaniProperties;
import com.billpayment.billpaydemo.dto.*;
import com.billpayment.billpaydemo.entity.BillEnquiryLog;
import com.billpayment.billpaydemo.entity.Client;
import com.billpayment.billpaydemo.exception.DuplicatedRequestIdException;
import com.billpayment.billpaydemo.exception.PaymentException;
import com.billpayment.billpaydemo.exception.UnAuthorizedException;
import com.billpayment.billpaydemo.repository.BillEnquiryLogRepository;
import com.billpayment.billpaydemo.repository.ClientRepository;
import com.billpayment.billpaydemo.service.BillEnquiryService;
import com.billpayment.billpaydemo.service.TokenDetailsService;
import lombok.SneakyThrows;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Date;

import static com.billpayment.billpaydemo.constants.ApiConstants.BILL_RECEIPT_V2;
import static com.billpayment.billpaydemo.constants.ApiConstants.BILL_STATEMENT;
import static com.billpayment.billpaydemo.constants.CommonConstants.AUTHORIZATION_HEADER;
import static com.billpayment.billpaydemo.constants.CommonConstants.AUTHORIZATION_HEADER_PREFIX;
import static com.billpayment.billpaydemo.constants.CommonConstants.BillEnquiryStatus.*;

@Service
@Transactional
public class BillEnquiryServiceImpl implements BillEnquiryService {

    private final BillEnquiryLogRepository billEnquiryLogRepository;
    private final RestTemplate restTemplate;
    private final TokenDetailsService tokenDetailsService;
    private final ClientRepository clientRepository;
    private final KhanepaniProperties khanepaniProperties;

    public BillEnquiryServiceImpl(BillEnquiryLogRepository billEnquiryLogRepository,
                                  RestTemplate restTemplate,
                                  TokenDetailsService tokenDetailsService,
                                  ClientRepository clientRepository,
                                  KhanepaniProperties khanepaniProperties) {
        this.billEnquiryLogRepository = billEnquiryLogRepository;
        this.restTemplate = restTemplate;
        this.tokenDetailsService = tokenDetailsService;
        this.clientRepository = clientRepository;
        this.khanepaniProperties = khanepaniProperties;
    }

    @Override
    public BillEnquiryResponseDTO fetchBillStatement(BillEnquiryRequestDTO billEnquiryRequestDTO) {
        if (!isClientValid(billEnquiryRequestDTO.getClientUsername(), billEnquiryRequestDTO.getPassword())) {
            throw new UnAuthorizedException("Username or password is incorrect.");
        }

        BillEnquiryLog billEnquiryLogByRequestId = findBillEnquiryLogByRequestId(billEnquiryRequestDTO.getRequestId());
        if (billEnquiryLogByRequestId != null) {
            throw new DuplicatedRequestIdException("RequestId must be unique.");
        }

        String token = tokenDetailsService.getToken(new TokenRequestDTO(
                khanepaniProperties.getUsername(),
                khanepaniProperties.getPassword()));

        BillStatementApiResponse billStatementApiResponse = fetchBillStatementDetails(billEnquiryRequestDTO, token);

        saveDataInBillEnquiryLog(billEnquiryRequestDTO, billStatementApiResponse);

        return buildBillEnquiryResponse(billEnquiryRequestDTO, billStatementApiResponse);
    }

    @SneakyThrows
    @Override
    @Transactional(dontRollbackOn = {PaymentException.class})
    public BillPaymentResponseDTO payBill(BillPaymentRequestDTO billPaymentRequestDTO) {
        if (!isClientValid(billPaymentRequestDTO.getClientUsername(), billPaymentRequestDTO.getPassword())) {
            throw new UnAuthorizedException("Username or password is incorrect.");
        }

        BillEnquiryLog billEnquiryLogByRequestId = findBillEnquiryLogByRequestId(billPaymentRequestDTO.getRequestId());
        if (billEnquiryLogByRequestId == null) {
            throw new Exception("Invalid Request Id.");
        }

        String token = tokenDetailsService.validateAndRetrieveToken();

        BillReceiptApiResponse billReceiptApiResponse = payBillAndUpdateBillEnquiryLog(billPaymentRequestDTO,
                billEnquiryLogByRequestId, token);

        return BillPaymentResponseDTO.builder()
                .responseCode(billReceiptApiResponse.getResponseCode())
                .messgae(billReceiptApiResponse.getStrValues())
                .requestId(billPaymentRequestDTO.getRequestId())
                .build();
    }

    private BillReceiptApiResponse payBillAndUpdateBillEnquiryLog(BillPaymentRequestDTO billPaymentRequestDTO, BillEnquiryLog billEnquiryLogByRequestId, String token) {
        BillReceiptApiResponse billReceiptApiResponse = null;
        String billLogStatus = null;
        try {
            billReceiptApiResponse = payAndGetReceipt(billPaymentRequestDTO, billEnquiryLogByRequestId, token);
            billLogStatus = PAYMENT_SUCCESS;
        } catch (Exception exception) {
            billLogStatus = PAYMENT_FAILURE;
            throw new PaymentException("Invalid token.");
        } finally {
            updateBillEnquiryLogStatus(billEnquiryLogByRequestId, billLogStatus);
        }
        return billReceiptApiResponse;
    }

    private BillEnquiryLog findBillEnquiryLogByRequestId(String requestId) {
        return billEnquiryLogRepository.findBillEnquiryLogByRequestId(
                requestId);
    }

    private void updateBillEnquiryLogStatus(BillEnquiryLog billEnquiryLogByRequestId, String status) {
        billEnquiryLogByRequestId.setStatus(status);
        billEnquiryLogRepository.save(billEnquiryLogByRequestId);
    }

    private BillEnquiryResponseDTO buildBillEnquiryResponse(BillEnquiryRequestDTO billEnquiryRequestDTO,
                                                            BillStatementApiResponse billStatementApiResponse) {
        return BillEnquiryResponseDTO.builder()
                .accountNo(billStatementApiResponse.getAccountNo())
                .advanceAvailable(billStatementApiResponse.getDetail().getAdvanceAvaliable())
                .amountPayable(billStatementApiResponse.getDetail().getAmountPayable())
                .customerCode(billEnquiryRequestDTO.getCustomerId())
                .customerName(billStatementApiResponse.getDetail().getCustomerName())
                .meterNumber(billStatementApiResponse.getDetail().getMeterNumber())
                .meterType(billStatementApiResponse.getDetail().getMeterType())
                .monthlyDetails(billStatementApiResponse.getDetail().getMonthlyDetails())
                .requestId(billEnquiryRequestDTO.getRequestId())
                .resultCode(billStatementApiResponse.getResponseCode())
                .resultDescription(billStatementApiResponse.getMessage())
                .sn(billStatementApiResponse.getSn())
                .totalBillAmount(billStatementApiResponse.getDetail().getTotalBillAmount())
                .totalDiscount(billStatementApiResponse.getDetail().getTotalDiscount())
                .totalFine(billStatementApiResponse.getDetail().getTotalFine())
                .build();
    }

    private void saveDataInBillEnquiryLog(BillEnquiryRequestDTO billEnquiryRequestDTO,
                                          BillStatementApiResponse billStatementApiResponse) {
        BillEnquiryLog billEnquiryLog = new BillEnquiryLog();
        billEnquiryLog.setCustomerId(billEnquiryRequestDTO.getCustomerId());
        billEnquiryLog.setMobileNumber(billEnquiryRequestDTO.getMobileNumber());
        billEnquiryLog.setRequestedBy(billEnquiryRequestDTO.getClientUsername());
        billEnquiryLog.setRequestedDate(new Date());
        billEnquiryLog.setStatus(ENQUIRY_SUCCESS);
        billEnquiryLog.setAccountNumber(billStatementApiResponse.getAccountNo());
        billEnquiryLog.setCustomerName(billStatementApiResponse.getDetail().getCustomerName());
        billEnquiryLog.setRequestId(billEnquiryRequestDTO.getRequestId());
        billEnquiryLog.setTransactionNumber(billStatementApiResponse.getSn());

        billEnquiryLogRepository.save(billEnquiryLog);
    }

    private boolean isClientValid(String username, String password) {
        Client client = clientRepository.findByUsername(username);
        if (client != null && client.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    private BillStatementApiResponse fetchBillStatementDetails(BillEnquiryRequestDTO billEnquiryRequestDTO,
                                                               String token) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(BILL_STATEMENT)
                .queryParam("customerId", billEnquiryRequestDTO.getCustomerId())
                .queryParam("mobileNo", billEnquiryRequestDTO.getMobileNumber());

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.set(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_PREFIX + token);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        BillStatementApiResponse billStatementApiResponse = null;

        try {
            ResponseEntity<BillStatementApiResponse> responseEntity = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    httpEntity,
                    BillStatementApiResponse.class);
            billStatementApiResponse = responseEntity.getBody();
        } catch (Exception e) {
            throw e;
        }
        return billStatementApiResponse;
    }

    private BillReceiptApiResponse payAndGetReceipt(BillPaymentRequestDTO billPaymentRequestDTO,
                                                    BillEnquiryLog billEnquiryLog,
                                                    String token) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(BILL_RECEIPT_V2)
                .queryParam("AccountNo", billEnquiryLog.getAccountNumber())
                .queryParam("Date", new Date())
                .queryParam("Opening", "0")
                .queryParam("Total", billPaymentRequestDTO.getTotalAmount())
                .queryParam("Advance", billPaymentRequestDTO.getAdvanceAmount())
                .queryParam("Paid", billPaymentRequestDTO.getPaidAmount())
                .queryParam("Fine", billPaymentRequestDTO.getFine())
                .queryParam("Discount", billPaymentRequestDTO.getDiscount())
                .queryParam("LastMonth", billPaymentRequestDTO.getMonthTo())
                .queryParam("LngBillCount", (billPaymentRequestDTO.getMonthTo() - billPaymentRequestDTO.getMonthFrom()) + 1);

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.set(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_PREFIX + token);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        BillReceiptApiResponse billReceiptApiResponse = null;

        try {
            ResponseEntity<BillReceiptApiResponse> responseEntity = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    httpEntity,
                    BillReceiptApiResponse.class);
            billReceiptApiResponse = responseEntity.getBody();
        } catch (Exception exception) {
            throw exception;
        }
        return billReceiptApiResponse;
    }
}
