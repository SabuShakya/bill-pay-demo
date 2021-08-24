package com.billpayment.billpaydemo.controller;

import com.billpayment.billpaydemo.dto.TokenRequestDTO;
import com.billpayment.billpaydemo.service.TokenDetailsService;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.billpayment.billpaydemo.constants.ApiConstants.BASE;
import static com.billpayment.billpaydemo.constants.ApiConstants.TokenConstants.GET_TOKEN;

@RestController
@RequestMapping(BASE)
@Api("Api Resources for token.")
public class TokenController {

    private final TokenDetailsService tokenDetailsService;

    public TokenController(TokenDetailsService tokenDetailsService) {
        this.tokenDetailsService = tokenDetailsService;
    }

    @PostMapping(GET_TOKEN)
    public ResponseEntity<String> getToken(@RequestBody TokenRequestDTO tokenRequestDTO) {
        return ResponseEntity.ok(tokenDetailsService.getToken(tokenRequestDTO));
    }
}
