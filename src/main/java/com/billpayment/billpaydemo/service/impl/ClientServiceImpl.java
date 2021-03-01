package com.billpayment.billpaydemo.service.impl;

import com.billpayment.billpaydemo.dto.UserRequestDTO;
import com.billpayment.billpaydemo.entity.Client;
import com.billpayment.billpaydemo.repository.ClientRepository;
import com.billpayment.billpaydemo.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public void addClientUser(UserRequestDTO userRequestDTO) {
        Client client = new Client();
        client.setUsername(userRequestDTO.getUsername());
        client.setPassword(new BCryptPasswordEncoder().encode(userRequestDTO.getPassword()));

        clientRepository.save(client);
    }
}
