package com.billpayment.billpaydemo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponseDTO {

    private String access_token;

    private String token_type;

    private String refresh_token;

    private String expires_in;

    private String scope;
}
