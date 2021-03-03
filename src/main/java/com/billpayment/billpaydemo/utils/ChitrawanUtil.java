package com.billpayment.billpaydemo.utils;

import com.billpayment.billpaydemo.configuration.proprties.ChitrawanProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;

public class ChitrawanUtil {

    public static HttpEntity getEntityWithHeaderForRequest(ChitrawanProperties chitrawanProperties,
                                                           MultiValueMap<String, String> requestData) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set(chitrawanProperties.getAuthKey(), chitrawanProperties.getAuthorizationHeader());

        HttpEntity httpEntity = new HttpEntity(requestData, httpHeaders);
        return httpEntity;
    }
}
