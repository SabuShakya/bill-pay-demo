package com.billpayment.billpaydemo.service;

import com.billpayment.billpaydemo.dto.LoginRequestDTO;
import com.billpayment.billpaydemo.dto.LoginResponse;

public interface LoginService {

    LoginResponse loginUser(LoginRequestDTO loginRequestDTO);
}
