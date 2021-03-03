package com.billpayment.billpaydemo.service.impl;

import com.billpayment.billpaydemo.configuration.proprties.ChitrawanProperties;
import com.billpayment.billpaydemo.dto.ChitrawanUserDetailsRequestDTO;
import com.billpayment.billpaydemo.dto.ChitrawanUserDetailsResponse;
import com.billpayment.billpaydemo.entity.ChitrawanRequestLog;
import com.billpayment.billpaydemo.exception.CustomException;
import com.billpayment.billpaydemo.exception.DuplicatedRequestIdException;
import com.billpayment.billpaydemo.repository.ChitrawanRequestLogRepository;
import com.billpayment.billpaydemo.service.ChitrawanUserDetailService;
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

import java.util.Objects;

import static com.billpayment.billpaydemo.constants.ApiConstants.ChitrawanVenderApiConstants.FETCH_USER_DETAILS;
import static com.billpayment.billpaydemo.constants.CommonConstants.BillEnquiryStatus.ENQUIRY_FAILURE;
import static com.billpayment.billpaydemo.constants.CommonConstants.BillEnquiryStatus.ENQUIRY_SUCCESS;

@Service
@AllArgsConstructor
public class ChitrawanUserDetailServiceImpl implements ChitrawanUserDetailService {

    private final ChitrawanProperties chitrawanProperties;

    private final RestTemplate restTemplate;

    private final ChitrawanRequestLogRepository chitrawanRequestLogRepository;

    @Override
    public ChitrawanUserDetailsResponse fetchUserDetails(ChitrawanUserDetailsRequestDTO userDetailsRequestDTO) {

        validateIfRequestIdAlreadyExists(userDetailsRequestDTO);

        ChitrawanUserDetailsResponse userDetailsFromChitrawanVendor = null;
        String status = null;

        try {
            userDetailsFromChitrawanVendor = getUserDetailsFromChitrawanVendor(userDetailsRequestDTO);
            status = userDetailsFromChitrawanVendor.isStatus() ? ENQUIRY_SUCCESS : ENQUIRY_FAILURE;
        } catch (Exception e) {
            status = ENQUIRY_FAILURE;
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            saveChitrawanDataToLog(userDetailsRequestDTO, userDetailsFromChitrawanVendor, status);
        }

        if (!(userDetailsFromChitrawanVendor.isStatus())) {
            throw new CustomException(userDetailsFromChitrawanVendor.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return userDetailsFromChitrawanVendor;
    }

    private void validateIfRequestIdAlreadyExists(ChitrawanUserDetailsRequestDTO userDetailsRequestDTO) {
        ChitrawanRequestLog requestLog = chitrawanRequestLogRepository.findByRequestId(userDetailsRequestDTO.getRequestId());

        if (requestLog != null)
            throw new DuplicatedRequestIdException("Request Id is invalid.");
    }

    private void saveChitrawanDataToLog(ChitrawanUserDetailsRequestDTO userDetailsRequestDTO,
                                        ChitrawanUserDetailsResponse userDetailsFromChitrawanVendor,
                                        String status) {
        ChitrawanRequestLog chitrawanRequestLog = new ChitrawanRequestLog();

        if (!Objects.isNull(userDetailsFromChitrawanVendor.getDetails())) {
            chitrawanRequestLog.setCustomerUsername(userDetailsFromChitrawanVendor.getDetails().getCustomer().getUsername());
            chitrawanRequestLog.setCustomerMobile(userDetailsFromChitrawanVendor.getDetails().getCustomer().getMobile());
            chitrawanRequestLog.setDueAmount(userDetailsFromChitrawanVendor.getDetails().getDue().getAmount());
        } else {
            chitrawanRequestLog.setCustomerUsername(userDetailsRequestDTO.getCustomerUsername());
        }
        chitrawanRequestLog.setRequestId(userDetailsRequestDTO.getRequestId());
        chitrawanRequestLog.setStatus(status);

        chitrawanRequestLogRepository.save(chitrawanRequestLog);
    }

    private ChitrawanUserDetailsResponse getUserDetailsFromChitrawanVendor(
            ChitrawanUserDetailsRequestDTO userDetailsRequestDTO) {
        MultiValueMap<String, String> requestData = new LinkedMultiValueMap<>();
        requestData.add("username", userDetailsRequestDTO.getCustomerUsername());

        HttpEntity httpEntity = ChitrawanUtil.getEntityWithHeaderForRequest(chitrawanProperties, requestData);

        ChitrawanUserDetailsResponse chitrawanUserDetailsResponse = new ChitrawanUserDetailsResponse();

        try {
            ResponseEntity<ChitrawanUserDetailsResponse> responseEntity = restTemplate.exchange(
                    FETCH_USER_DETAILS,
                    HttpMethod.POST,
                    httpEntity,
                    ChitrawanUserDetailsResponse.class
            );

            if (Objects.requireNonNull(responseEntity.getBody()).isStatus()) {
                chitrawanUserDetailsResponse.setDetails(responseEntity.getBody().getDetails());
            }

            chitrawanUserDetailsResponse.setCode(responseEntity.getBody().getCode());
            chitrawanUserDetailsResponse.setStatus(responseEntity.getBody().isStatus());
            chitrawanUserDetailsResponse.setMessage(responseEntity.getBody().getMessage());

        } catch (Exception e) {
            throw e;
        }

        return chitrawanUserDetailsResponse;
    }
}
