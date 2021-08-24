package com.billpayment.billpaydemo.service;

import com.billpayment.billpaydemo.entity.Client;
import com.billpayment.billpaydemo.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class UserAuthenticationService implements UserDetailsService {

    private final ClientRepository clientRepository;

    /*
     * The core idea is to return the User instance with populated values
     * which will be used by the authentication manager to authenticate.
     * so it acts as a provider
     * */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client user = clientRepository.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("User " + username + " not found!");

        UserDetails userDetails = new User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
        return userDetails;
    }
}
