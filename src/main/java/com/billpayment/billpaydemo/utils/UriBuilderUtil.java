package com.billpayment.billpaydemo.utils;

import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

import static com.billpayment.billpaydemo.constants.ApiConstants.BILL_STATEMENT;

public class UriBuilderUtil {

    public static String getUriWithQueryParameters(String uri, Map<String, String> queryparams) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(uri);
        queryparams.forEach((key, value) -> uriBuilder.queryParam(key, value));
        return uriBuilder.toUriString();
    }
}
