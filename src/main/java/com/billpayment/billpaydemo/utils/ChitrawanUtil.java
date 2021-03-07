package com.billpayment.billpaydemo.utils;

import com.billpayment.billpaydemo.configuration.proprties.ChitrawanProperties;
import com.billpayment.billpaydemo.dto.*;
import com.billpayment.billpaydemo.entity.ChitrawanRequestLog;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.billpayment.billpaydemo.constants.CommonConstants.BillEnquiryStatus.PAYMENT_SUCCESS;

public class ChitrawanUtil {

    public static BiFunction<ChitrawanProperties, MultiValueMap<String, String>, HttpEntity>
            getEntityWithHeaderForRequest = (chitrawanProperties, requestData) -> {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set(chitrawanProperties.getAuthKey(), chitrawanProperties.getAuthorizationHeader());

        HttpEntity httpEntity = new HttpEntity(requestData, httpHeaders);
        return httpEntity;
    };

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

    public static Function<ResponseEntity<ChitrawanUserDetailsResponse>, ChitrawanUserDetailsResponse>
            convertResponseEntityToUserDetailsResponse = (responseEntity) -> {

        ChitrawanUserDetailsResponse chitrawanUserDetailsResponse = new ChitrawanUserDetailsResponse();

        if (Objects.requireNonNull(responseEntity.getBody()).isStatus()) {
            chitrawanUserDetailsResponse.setDetails(responseEntity.getBody().getDetails());
        }

        chitrawanUserDetailsResponse.setCode(responseEntity.getBody().getCode());
        chitrawanUserDetailsResponse.setStatus(responseEntity.getBody().isStatus());
        chitrawanUserDetailsResponse.setMessage(responseEntity.getBody().getMessage());

        return chitrawanUserDetailsResponse;
    };

    public static void updateRequestLogData(ChitrawanPaymentRequestDTO chitrawanPaymentRequestDTO,
                                            ChitrawanRequestLog requestLog,
                                            String status) {
        requestLog.setStatus(status);
        requestLog.setAmountPaid(status.equals(PAYMENT_SUCCESS) ? chitrawanPaymentRequestDTO.getAmount() : 0);
        requestLog.setTransactionId(chitrawanPaymentRequestDTO.getTransactionId());
    }

    public static ChitrawanUserDetailResponseDTO parseToUserDetailResponseDTO(
            ChitrawanUserDetailsResponse userDetailsFromChitrawanVendor,
            String requestId
    ) {

        ChitrawanResponseDetails details = userDetailsFromChitrawanVendor.getDetails();

        List<PackageResponseDTO> packageResponseDTOS = details.getPackages()
                .stream()
                .map(chitrawanPackages -> new PackageResponseDTO(
                        chitrawanPackages.getPackage_id(),
                        chitrawanPackages.getPackage_name(),
                        chitrawanPackages.getAmount(),
                        chitrawanPackages.getCurrent()
                ))
                .collect(Collectors.toList());

        return ChitrawanUserDetailResponseDTO.builder()
                .userId(details.getCustomer().getUsername())
                .name(details.getCustomer().getName())
                .mobileNo(details.getCustomer().getMobile())
                .accountStatus(details.getCustomer().getStatus())
                .plan(details.getCurrent_subscription().getPackage())
                .expiryDate(details.getCurrent_subscription().getEnd_date())
                .planStatus(details.getCurrent_subscription().getStatus())
                .requestId(requestId)
                .payableAmount(details.getDue().getAmount())
                .packages(packageResponseDTOS)
                .build();
    }

}
