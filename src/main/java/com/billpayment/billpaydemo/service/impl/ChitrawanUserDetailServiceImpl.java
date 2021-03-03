package com.billpayment.billpaydemo.service.impl;

import com.billpayment.billpaydemo.configuration.proprties.ChitrawanProperties;
import com.billpayment.billpaydemo.dto.ChitrawanUserDetailsRequestDTO;
import com.billpayment.billpaydemo.dto.ChitrawanUserDetailsResponse;
import com.billpayment.billpaydemo.dto.GenericResponseDTO;
import com.billpayment.billpaydemo.entity.ChitrawanRequestLog;
import com.billpayment.billpaydemo.exception.CustomException;
import com.billpayment.billpaydemo.exception.DataDuplicateException;
import com.billpayment.billpaydemo.service.ChitrawanRequestLogService;
import com.billpayment.billpaydemo.service.ChitrawanUserDetailService;
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
import static com.billpayment.billpaydemo.utils.ChitrawanUtil.*;

@Service
@AllArgsConstructor
public class ChitrawanUserDetailServiceImpl implements ChitrawanUserDetailService {

    private final ChitrawanProperties chitrawanProperties;

    private final RestTemplate restTemplate;

    private final ChitrawanRequestLogService chitrawanRequestLogService;

    @Override
    public GenericResponseDTO fetchUserDetails(ChitrawanUserDetailsRequestDTO userDetailsRequestDTO) {

        validateRequestIdDuplicity(userDetailsRequestDTO);

        ChitrawanUserDetailsResponse userDetailsFromChitrawanVendor =
                getChitrawanUserDetailsAndSaveDataInLog(userDetailsRequestDTO);

        if (!(userDetailsFromChitrawanVendor.isStatus())) {
            throw new CustomException(userDetailsFromChitrawanVendor.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return GenericResponseDTO.builder()
                .resultCode(userDetailsFromChitrawanVendor.getCode())
                .resultDescription("User details obtained")
                .data(parseToUserDetailResponseDTO(userDetailsFromChitrawanVendor,userDetailsRequestDTO.getRequestId()))
                .build();
    }

    private ChitrawanUserDetailsResponse getChitrawanUserDetailsAndSaveDataInLog(
            ChitrawanUserDetailsRequestDTO userDetailsRequestDTO) {

        String status = null;
        ChitrawanUserDetailsResponse userDetailsFromChitrawanVendor = null;

        try {
            userDetailsFromChitrawanVendor = getUserDetailsFromChitrawanVendor(userDetailsRequestDTO);
            status = userDetailsFromChitrawanVendor.isStatus() ? ENQUIRY_SUCCESS : ENQUIRY_FAILURE;
        } catch (Exception e) {
            status = ENQUIRY_FAILURE;
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            saveChitrawanDataToLog(userDetailsRequestDTO, userDetailsFromChitrawanVendor, status);
        }

        return userDetailsFromChitrawanVendor;
    }

    private void validateRequestIdDuplicity(ChitrawanUserDetailsRequestDTO userDetailsRequestDTO) {
        ChitrawanRequestLog requestLog = chitrawanRequestLogService.findChitrawanRequestLogByRequestId(
                userDetailsRequestDTO.getRequestId());

        if (Objects.nonNull(requestLog))
            throw new DataDuplicateException("Request Id is invalid.");
    }

    private void saveChitrawanDataToLog(ChitrawanUserDetailsRequestDTO userDetailsRequestDTO,
                                        ChitrawanUserDetailsResponse userDetailsFromChitrawanVendor,
                                        String status) {

        ChitrawanRequestLog requestLog = parseToRequestLog(userDetailsFromChitrawanVendor, userDetailsRequestDTO, status);

        chitrawanRequestLogService.saveChitrawanRequestLog(requestLog);
    }

    private ChitrawanUserDetailsResponse getUserDetailsFromChitrawanVendor(
            ChitrawanUserDetailsRequestDTO userDetailsRequestDTO) {

        MultiValueMap<String, String> requestData = new LinkedMultiValueMap<>();
        requestData.add("username", userDetailsRequestDTO.getCustomerUsername());

        HttpEntity httpEntity = getEntityWithHeaderForRequest.apply(chitrawanProperties, requestData);

        ResponseEntity<ChitrawanUserDetailsResponse> responseEntity = null;

        try {
            responseEntity = restTemplate.exchange(
                    FETCH_USER_DETAILS,
                    HttpMethod.POST,
                    httpEntity,
                    ChitrawanUserDetailsResponse.class
            );
        } catch (Exception e) {
            throw e;
        }

        return convertResponseEntityToUserDetailsResponse.apply(responseEntity);
    }
}
