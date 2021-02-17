package com.billpayment.billpaydemo.service.impl;

import com.billpayment.billpaydemo.dto.*;
import com.billpayment.billpaydemo.entity.BillEnquiryLog;
import com.billpayment.billpaydemo.entity.Client;
import com.billpayment.billpaydemo.exception.UnAuthorizedException;
import com.billpayment.billpaydemo.repository.BillEnquiryLogRepository;
import com.billpayment.billpaydemo.repository.ClientRepository;
import com.billpayment.billpaydemo.repository.TokenDetailsRepository;
import com.billpayment.billpaydemo.service.BillEnquiryService;
import com.billpayment.billpaydemo.service.TokenDetailsService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Date;

import static com.billpayment.billpaydemo.constants.ApiConstants.BILL_STATEMENT;
import static com.billpayment.billpaydemo.constants.CommonConstants.*;
import static com.billpayment.billpaydemo.constants.CommonConstants.BillEnquiryStatus.ENQUIRY_SUCCESS;

@Service
@Transactional
public class BillEnquiryServiceImpl implements BillEnquiryService {

    private final BillEnquiryLogRepository billEnquiryLogRepository;
    private final TokenDetailsRepository tokenDetailsRepository;
    private final RestTemplate restTemplate;
    private final TokenDetailsService tokenDetailsService;
    private final ClientRepository clientRepository;

    public BillEnquiryServiceImpl(BillEnquiryLogRepository billEnquiryLogRepository,
                                  TokenDetailsRepository tokenDetailsRepository,
                                  RestTemplate restTemplate,
                                  TokenDetailsService tokenDetailsService,
                                  ClientRepository clientRepository) {
        this.billEnquiryLogRepository = billEnquiryLogRepository;
        this.tokenDetailsRepository = tokenDetailsRepository;
        this.restTemplate = restTemplate;
        this.tokenDetailsService = tokenDetailsService;
        this.clientRepository = clientRepository;
    }

    @Override
    public BillEnquiryResponseDTO fetchBillStatement(BillEnquiryRequestDTO billEnquiryRequestDTO) {
        if (!isClientValid(billEnquiryRequestDTO)) {
            throw new UnAuthorizedException("Username or password is incorrect.");
        }

        String token = tokenDetailsService.getToken(new TokenRequestDTO(USERNAME, PASSWORD));

        BillStatementApiResponse billStatementApiResponse = fetchBillStatementDetails(billEnquiryRequestDTO, token);

        saveDataInBillEnquiryLog(billEnquiryRequestDTO, billStatementApiResponse);

        return buildBillEnquiryResponse(billEnquiryRequestDTO, billStatementApiResponse);
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

    private boolean isClientValid(BillEnquiryRequestDTO billEnquiryRequestDTO) {
        Client client = clientRepository.findByUsername(billEnquiryRequestDTO.getClientUsername());
        if (client != null && client.getPassword().equals(billEnquiryRequestDTO.getPassword())) {
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
}
