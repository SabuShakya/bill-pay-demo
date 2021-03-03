package com.billpayment.billpaydemo.service.impl;

import com.billpayment.billpaydemo.configuration.proprties.ChitrawanProperties;
import com.billpayment.billpaydemo.dto.ChitrawanPaymentActivateResponseDTO;
import com.billpayment.billpaydemo.dto.ChitrawanPaymentRequestDTO;
import com.billpayment.billpaydemo.dto.ChitrawanTxnStatusRequestDTO;
import com.billpayment.billpaydemo.entity.ChitrawanRequestLog;
import com.billpayment.billpaydemo.exception.CustomException;
import com.billpayment.billpaydemo.exception.DataDuplicateException;
import com.billpayment.billpaydemo.repository.ChitrawanRequestLogRepository;
import com.billpayment.billpaydemo.service.ChitrawanPaymentService;
import com.billpayment.billpaydemo.service.ChitrawanRequestLogService;
import com.billpayment.billpaydemo.utils.ChitrawanUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static com.billpayment.billpaydemo.constants.ApiConstants.ChitrawanVenderApiConstants.ACTIVATE_PAYMENT;
import static com.billpayment.billpaydemo.constants.ApiConstants.ChitrawanVenderApiConstants.CHECK_TRANSACTION_STATUS;
import static com.billpayment.billpaydemo.constants.CommonConstants.BillEnquiryStatus.PAYMENT_FAILURE;
import static com.billpayment.billpaydemo.constants.CommonConstants.BillEnquiryStatus.PAYMENT_SUCCESS;
import static com.billpayment.billpaydemo.utils.ChitrawanUtil.getEntityWithHeaderForRequest;
import static com.billpayment.billpaydemo.utils.ChitrawanUtil.updateRequestLogData;

@Service
@AllArgsConstructor
public class ChitrawanPaymentServiceImpl implements ChitrawanPaymentService {

    private final ChitrawanProperties chitrawanProperties;
    private final ChitrawanRequestLogRepository chitrawanRequestLogRepository;
    private final RestTemplate restTemplate;
    private final ChitrawanRequestLogService chitrawanRequestLogService;

    @Override
    public ChitrawanPaymentActivateResponseDTO activatePayment(ChitrawanPaymentRequestDTO chitrawanPaymentRequestDTO) {

        ChitrawanRequestLog requestLog = chitrawanRequestLogService.findChitrawanRequestLogByRequestId(
                chitrawanPaymentRequestDTO.getRequestId());

        ChitrawanPaymentActivateResponseDTO paymentActivateResponseDTO = null;

        if (requestLog != null) {
            paymentActivateResponseDTO = makePaymentAndUpdateLog(chitrawanPaymentRequestDTO,
                    requestLog);
        } else {
            throw new DataDuplicateException("Invalid Request Id.");
        }

        return checkStatusAndPrepareReturnValue(paymentActivateResponseDTO);
    }

    private ChitrawanPaymentActivateResponseDTO checkStatusAndPrepareReturnValue(
            ChitrawanPaymentActivateResponseDTO paymentActivateResponseDTO) {
        if (paymentActivateResponseDTO.isStatus())
            return paymentActivateResponseDTO;
        else
            throw new CustomException(paymentActivateResponseDTO.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ChitrawanPaymentActivateResponseDTO makePaymentAndUpdateLog(ChitrawanPaymentRequestDTO chitrawanPaymentRequestDTO,
                                                                        ChitrawanRequestLog requestLog) {

        ChitrawanPaymentActivateResponseDTO paymentActivateResponseDTO = null;
        String status = null;

        try {
            paymentActivateResponseDTO = makePaymentToChitrawan(chitrawanPaymentRequestDTO);

            status = paymentActivateResponseDTO.isStatus() ? PAYMENT_SUCCESS : PAYMENT_FAILURE;

            requestLog.setReferenceCode(paymentActivateResponseDTO.isStatus()
                    ? paymentActivateResponseDTO.getReference_code() : null);

        } catch (Exception e) {
            status = PAYMENT_FAILURE;
        } finally {
            updateChitrawanRequestLog(chitrawanPaymentRequestDTO, requestLog, status);
        }

        return paymentActivateResponseDTO;
    }

    private void updateChitrawanRequestLog(ChitrawanPaymentRequestDTO chitrawanPaymentRequestDTO,
                                           ChitrawanRequestLog requestLog,
                                           String status) {

        updateRequestLogData(chitrawanPaymentRequestDTO, requestLog, status);

        chitrawanRequestLogService.saveChitrawanRequestLog(requestLog);
    }

    @Override
    public ChitrawanPaymentActivateResponseDTO checkTransactionStatus(
            ChitrawanTxnStatusRequestDTO chitrawanTxnStatusRequestDTO) {

        validateIfTxnIdAndAmountIsValid(chitrawanTxnStatusRequestDTO);

        ChitrawanPaymentActivateResponseDTO paymentActivateResponseDTO
                = makeTransactionStatusRequestToChitrawan(chitrawanTxnStatusRequestDTO);

        return checkStatusAndPrepareReturnValue(paymentActivateResponseDTO);
    }

    private void validateIfTxnIdAndAmountIsValid(ChitrawanTxnStatusRequestDTO chitrawanTxnStatusRequestDTO) {
        Optional<ChitrawanRequestLog> requestLog =
                chitrawanRequestLogService.findChitrawanRequestLogByTransactionIdAndAmountPaid(
                        chitrawanTxnStatusRequestDTO.getTransactionId(),
                        chitrawanTxnStatusRequestDTO.getAmount()
                );

        if (!requestLog.isPresent())
            throw new CustomException("Invalid Transaction Id or amount.", HttpStatus.BAD_REQUEST);
    }


    private ChitrawanPaymentActivateResponseDTO makeTransactionStatusRequestToChitrawan(
            ChitrawanTxnStatusRequestDTO chitrawanTxnStatusRequestDTO) {
        MultiValueMap<String, String> requestData = new LinkedMultiValueMap<>();

        requestData.add("amount", String.valueOf(chitrawanTxnStatusRequestDTO.getAmount()));
        requestData.add("transaction_id", chitrawanTxnStatusRequestDTO.getTransactionId());
        requestData.add("username", chitrawanTxnStatusRequestDTO.getUsername());

        HttpEntity httpEntity = getEntityWithHeaderForRequest.apply(chitrawanProperties, requestData);
        ChitrawanPaymentActivateResponseDTO paymentActivateResponseDTO = null;

        try {
            ResponseEntity<ChitrawanPaymentActivateResponseDTO> responseEntity = restTemplate.postForEntity(
                    CHECK_TRANSACTION_STATUS,
                    httpEntity,
                    ChitrawanPaymentActivateResponseDTO.class
            );
            paymentActivateResponseDTO = responseEntity.getBody();
        } catch (Exception exception) {
            throw new CustomException(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return paymentActivateResponseDTO;
    }

    private ChitrawanPaymentActivateResponseDTO makePaymentToChitrawan(
            ChitrawanPaymentRequestDTO chitrawanPaymentRequestDTO) {
        MultiValueMap<String, String> requestData = new LinkedMultiValueMap<>();
        requestData.add("package_id", chitrawanPaymentRequestDTO.getPackageId());
        requestData.add("amount", String.valueOf(chitrawanPaymentRequestDTO.getAmount()));
        requestData.add("transaction_id", chitrawanPaymentRequestDTO.getTransactionId());
        requestData.add("username", chitrawanPaymentRequestDTO.getUsername());

        HttpEntity httpEntity = getEntityWithHeaderForRequest.apply(chitrawanProperties, requestData);

        ChitrawanPaymentActivateResponseDTO paymentActivateResponseDTO = null;

        try {
            ResponseEntity<ChitrawanPaymentActivateResponseDTO> responseEntity =
                    restTemplate.exchange(
                            ACTIVATE_PAYMENT,
                            HttpMethod.POST,
                            httpEntity,
                            ChitrawanPaymentActivateResponseDTO.class
                    );
            paymentActivateResponseDTO = responseEntity.getBody();
        } catch (Exception exception) {
            System.out.println("++++++++" + exception);
            throw new CustomException(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return paymentActivateResponseDTO;
    }
}
