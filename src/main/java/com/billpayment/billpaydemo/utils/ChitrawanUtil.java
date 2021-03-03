package com.billpayment.billpaydemo.utils;

import com.billpayment.billpaydemo.configuration.proprties.ChitrawanProperties;
import com.billpayment.billpaydemo.dto.ChitrawanUserDetailsRequestDTO;
import com.billpayment.billpaydemo.dto.ChitrawanUserDetailsResponse;
import com.billpayment.billpaydemo.entity.ChitrawanRequestLog;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.util.Objects;

public class ChitrawanUtil {

    public static HttpEntity getEntityWithHeaderForRequest(ChitrawanProperties chitrawanProperties,
                                                           MultiValueMap<String, String> requestData) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set(chitrawanProperties.getAuthKey(), chitrawanProperties.getAuthorizationHeader());

        HttpEntity httpEntity = new HttpEntity(requestData, httpHeaders);
        return httpEntity;
    }

    public static ChitrawanRequestLog parseToRequestLog(ChitrawanUserDetailsResponse userDetailsFromChitrawanVendor,
                                                        ChitrawanUserDetailsRequestDTO userDetailsRequestDTO,
                                                        String status) {

        ChitrawanRequestLog chitrawanRequestLog = new ChitrawanRequestLog();

        if (!Objects.nonNull(userDetailsFromChitrawanVendor.getDetails())) {
            chitrawanRequestLog.setCustomerUsername(userDetailsFromChitrawanVendor.getDetails().getCustomer().getUsername());
            chitrawanRequestLog.setCustomerMobile(userDetailsFromChitrawanVendor.getDetails().getCustomer().getMobile());
            chitrawanRequestLog.setDueAmount(userDetailsFromChitrawanVendor.getDetails().getDue().getAmount());
        } else
            chitrawanRequestLog.setCustomerUsername(userDetailsRequestDTO.getCustomerUsername());

        chitrawanRequestLog.setRequestId(userDetailsRequestDTO.getRequestId());
        chitrawanRequestLog.setStatus(status);

        return chitrawanRequestLog;
    }

    public static ChitrawanUserDetailsResponse convertResponseEntityToUserDetailsResponse(
            ResponseEntity<ChitrawanUserDetailsResponse> responseEntity) {

        ChitrawanUserDetailsResponse chitrawanUserDetailsResponse = new ChitrawanUserDetailsResponse();

        if (Objects.requireNonNull(responseEntity.getBody()).isStatus()) {
            chitrawanUserDetailsResponse.setDetails(responseEntity.getBody().getDetails());
        }

        chitrawanUserDetailsResponse.setCode(responseEntity.getBody().getCode());
        chitrawanUserDetailsResponse.setStatus(responseEntity.getBody().isStatus());
        chitrawanUserDetailsResponse.setMessage(responseEntity.getBody().getMessage());

        return chitrawanUserDetailsResponse;
    }

}
