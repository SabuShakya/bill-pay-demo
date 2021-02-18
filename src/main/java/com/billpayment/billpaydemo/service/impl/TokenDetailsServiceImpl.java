package com.billpayment.billpaydemo.service.impl;

import com.billpayment.billpaydemo.configuration.proprties.KhanepaniProperties;
import com.billpayment.billpaydemo.dto.TokenRequestDTO;
import com.billpayment.billpaydemo.dto.TokenResponseDTO;
import com.billpayment.billpaydemo.entity.TokenDetails;
import com.billpayment.billpaydemo.exception.TokenUnavailableException;
import com.billpayment.billpaydemo.exception.UnAuthorizedException;
import com.billpayment.billpaydemo.repository.TokenDetailsRepository;
import com.billpayment.billpaydemo.service.TokenDetailsService;
import com.billpayment.billpaydemo.utils.TokenValidationUtil;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.billpayment.billpaydemo.constants.ApiConstants.FETCH_TOKEN;
import static com.billpayment.billpaydemo.constants.CommonConstants.AUTHORIZATION_HEADER;

@Service
@Transactional
public class TokenDetailsServiceImpl implements TokenDetailsService {

    private final RestTemplate restTemplate;
    private final TokenDetailsRepository tokenDetailsRepository;
    private final KhanepaniProperties khanepaniProperties;

    public TokenDetailsServiceImpl(RestTemplate restTemplate,
                                   TokenDetailsRepository tokenDetailsRepository, KhanepaniProperties khanepaniProperties) {
        this.restTemplate = restTemplate;
        this.tokenDetailsRepository = tokenDetailsRepository;
        this.khanepaniProperties = khanepaniProperties;
    }

    @Override
    public String getToken(TokenRequestDTO tokenRequestDTO) {
        TokenResponseDTO tokenResponseDTO = fetchTokenDetails(tokenRequestDTO);
        saveTokenDetails(tokenResponseDTO);
        return tokenResponseDTO.getAccess_token();
    }

    @Override
    public String validateAndRetrieveToken() {
        TokenDetails activeToken = findActiveToken();
        if (activeToken == null) {
            TokenResponseDTO tokenResponseDTO = fetchTokenDetails(new TokenRequestDTO(
                    khanepaniProperties.getUsername(),
                    khanepaniProperties.getPassword()));
            return tokenResponseDTO.getAccess_token();

        } else if (activeToken != null && !TokenValidationUtil.isTokenExpired(activeToken)) {
            return activeToken.getAccessToken();

        } else {
            throw new TokenUnavailableException("Invalid token!");
        }
    }

    private TokenResponseDTO fetchTokenDetails(TokenRequestDTO tokenRequestDTO) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(FETCH_TOKEN)
                .queryParam("grant_type", khanepaniProperties.getGrantType())
                .queryParam("username", tokenRequestDTO.getUsername())
                .queryParam("password", tokenRequestDTO.getPassword());

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.set(AUTHORIZATION_HEADER, "Basic " + khanepaniProperties.getAuthorizationHeader());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        TokenResponseDTO tokenResponseDTO = null;
        try {
            ResponseEntity<TokenResponseDTO> responseEntity = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.POST,
                    httpEntity,
                    TokenResponseDTO.class);
            tokenResponseDTO = responseEntity.getBody();
        } catch (Exception e) {
            throw new UnAuthorizedException("Incorrect Username or Password.");
        }
        return tokenResponseDTO;
    }

    public void saveTokenDetails(TokenResponseDTO tokenResponseDTO) {
        List<TokenDetails> tokenDetailsList = new ArrayList<>();

        findActiveTokenAndSetInactive(tokenDetailsList);

        TokenDetails tokenDetails = new TokenDetails();

        tokenDetails.setAccessToken(tokenResponseDTO.getAccess_token());
        tokenDetails.setRefresh_token(tokenResponseDTO.getRefresh_token());
        tokenDetails.setExpiresIn(Integer.valueOf(tokenResponseDTO.getExpires_in()));
        tokenDetails.setTokenType(tokenResponseDTO.getToken_type());
        tokenDetails.setScope(tokenResponseDTO.getScope());
        tokenDetails.setRequestedDate(new Date());
        tokenDetails.setStatus('Y');

        tokenDetailsList.add(tokenDetails);
        tokenDetailsRepository.saveAll(tokenDetailsList);
    }

    private void findActiveTokenAndSetInactive(List<TokenDetails> tokenDetailsList) {
        TokenDetails activeToken = findActiveToken();
        if (activeToken != null) {
            activeToken.setStatus('N');
            tokenDetailsList.add(activeToken);
        }
    }

    public TokenDetails findActiveToken() {
        return tokenDetailsRepository.findByStatus('Y');
    }
}
