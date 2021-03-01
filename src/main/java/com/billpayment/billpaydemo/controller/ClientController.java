package com.billpayment.billpaydemo.controller;

import com.billpayment.billpaydemo.dto.UserRequestDTO;
import com.billpayment.billpaydemo.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.billpayment.billpaydemo.constants.ApiConstants.BASE;
import static com.billpayment.billpaydemo.constants.ApiConstants.ClientApiConstants.CREATE_CLIENT;

@RestController
@RequestMapping(BASE)
@AllArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping(CREATE_CLIENT)
    private ResponseEntity<?> createClient(@RequestBody UserRequestDTO user) {
        clientService.addClientUser(user);
        return new ResponseEntity<>("User created successfully.", HttpStatus.OK);
    }
}
