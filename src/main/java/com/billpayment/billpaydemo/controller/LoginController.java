package com.billpayment.billpaydemo.controller;

import com.billpayment.billpaydemo.dto.LoginRequestDTO;
import com.billpayment.billpaydemo.dto.LoginResponse;
import com.billpayment.billpaydemo.dto.UserRequestDTO;
import com.billpayment.billpaydemo.service.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    private ResponseEntity<?> loginUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(loginService.loginUser(loginRequestDTO));
    }


}
