package com.billpayment.billpaydemo.service.impl;

import com.billpayment.billpaydemo.dto.LoginRequestDTO;
import com.billpayment.billpaydemo.dto.LoginResponse;
import com.billpayment.billpaydemo.exception.UnAuthorizedException;
import com.billpayment.billpaydemo.service.LoginService;
import com.billpayment.billpaydemo.service.UserAuthenticationService;
import com.billpayment.billpaydemo.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserAuthenticationService userAuthenticationService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse loginUser(LoginRequestDTO loginRequestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getUsername(),
                            loginRequestDTO.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new UnAuthorizedException("Invalid user.");
        }
        UserDetails userDetails = userAuthenticationService.loadUserByUsername(loginRequestDTO.getUsername());
        String jwtToken = "Bearer " + jwtUtil.generateToken(userDetails);
        return new LoginResponse(jwtToken);
    }
}
