package com.billpayment.billpaydemo.service;

import com.billpayment.billpaydemo.dto.TokenRequestDTO;

public interface TokenDetailsService {

    public String getToken(TokenRequestDTO tokenRequestDTO);

    public String validateAndRetrieveToken();
}
