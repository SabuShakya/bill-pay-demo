package com.billpayment.billpaydemo.service;

import com.billpayment.billpaydemo.dto.ChitrawanUserDetailsRequestDTO;
import com.billpayment.billpaydemo.dto.GenericResponseDTO;

public interface ChitrawanUserDetailService {

    GenericResponseDTO fetchUserDetails(ChitrawanUserDetailsRequestDTO userDetailsRequestDTO);
}
