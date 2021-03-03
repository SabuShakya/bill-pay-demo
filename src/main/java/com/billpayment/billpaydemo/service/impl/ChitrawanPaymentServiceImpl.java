package com.billpayment.billpaydemo.service.impl;

import com.billpayment.billpaydemo.configuration.proprties.ChitrawanProperties;
import com.billpayment.billpaydemo.constants.ApiConstants;
import com.billpayment.billpaydemo.dto.ChitrawanPaymentActivateResponseDTO;
import com.billpayment.billpaydemo.dto.ChitrawanPaymentRequestDTO;
import com.billpayment.billpaydemo.entity.ChitrawanRequestLog;
import com.billpayment.billpaydemo.exception.CustomException;
import com.billpayment.billpaydemo.exception.DuplicatedRequestIdException;
import com.billpayment.billpaydemo.repository.ChitrawanRequestLogRepository;
import com.billpayment.billpaydemo.service.ChitrawanPaymentService;
import com.billpayment.billpaydemo.utils.ChitrawanUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static com.billpayment.billpaydemo.constants.ApiConstants.ChitrawanVenderApiConstants.ACTIVATE_PAYMENT;
import static com.billpayment.billpaydemo.constants.CommonConstants.BillEnquiryStatus.PAYMENT_FAILURE;
import static com.billpayment.billpaydemo.constants.CommonConstants.BillEnquiryStatus.PAYMENT_SUCCESS;

@Service
@AllArgsConstructor
public class ChitrawanPaymentServiceImpl implements ChitrawanPaymentService {

    private final ChitrawanProperties chitrawanProperties;
    private final ChitrawanRequestLogRepository chitrawanRequestLogRepository;
    private final RestTemplate restTemplate;

    @Override
    public ChitrawanPaymentActivateResponseDTO activatePayment(ChitrawanPaymentRequestDTO chitrawanPaymentRequestDTO) {

        ChitrawanRequestLog requestLog = chitrawanRequestLogRepository.findByRequestId(
                chitrawanPaymentRequestDTO.getRequestId());
        ChitrawanPaymentActivateResponseDTO paymentActivateResponseDTO = null;

        if (requestLog != null) {
            try {
                paymentActivateResponseDTO = makePaymentToChitrawan(chitrawanPaymentRequestDTO);
                requestLog.setStatus(paymentActivateResponseDTO.isStatus() ? PAYMENT_SUCCESS : PAYMENT_FAILURE);
                requestLog.setAmountPaid(paymentActivateResponseDTO.isStatus() ? chitrawanPaymentRequestDTO.getAmount()
                        : 0);
            } catch (Exception e) {
                requestLog.setStatus(PAYMENT_FAILURE);
            } finally {
                requestLog.setTransactionId(chitrawanPaymentRequestDTO.getTransactionId());
                chitrawanRequestLogRepository.save(requestLog);
            }
        } else {
            throw new DuplicatedRequestIdException("Invalid Request Id.");
        }

        if (paymentActivateResponseDTO.isStatus())
            return paymentActivateResponseDTO;
        else
            throw new CustomException(paymentActivateResponseDTO.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ChitrawanPaymentActivateResponseDTO makePaymentToChitrawan(ChitrawanPaymentRequestDTO chitrawanPaymentRequestDTO) {
        MultiValueMap<String, String> requestData = new LinkedMultiValueMap<>();
        requestData.add("package_id", chitrawanPaymentRequestDTO.getPackageId());
        requestData.add("amount", String.valueOf(chitrawanPaymentRequestDTO.getAmount()));
        requestData.add("transaction_id", chitrawanPaymentRequestDTO.getTransactionId());
        requestData.add("username", chitrawanPaymentRequestDTO.getUsername());

        HttpEntity httpEntity = ChitrawanUtil.getEntityWithHeaderForRequest(chitrawanProperties, requestData);

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
