package com.billpayment.billpaydemo.service;

import com.billpayment.billpaydemo.dto.ChitrawanUserDetailsRequestDTO;
import com.billpayment.billpaydemo.dto.ChitrawanUserDetailsResponse;

public interface ChitrawanUserDetailService {

    ChitrawanUserDetailsResponse fetchUserDetails(ChitrawanUserDetailsRequestDTO userDetailsRequestDTO);
}
