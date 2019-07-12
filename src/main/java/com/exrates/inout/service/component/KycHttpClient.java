package com.exrates.inout.service.component;

import com.exrates.inout.domain.constants.Constants;
import com.exrates.inout.domain.dto.AccountQuberaRequestDto;
import com.exrates.inout.domain.dto.AccountQuberaResponseDto;
import com.exrates.inout.exceptions.NgDashboardException;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.properties.models.QuberaConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

//@Log4j2
@Component
public class KycHttpClient {

    private static final Logger log = LogManager.getLogger(KycHttpClient.class);

    private String uriApi;
    private String apiKey;

    private RestTemplate template;

    @Autowired
    public KycHttpClient(QuberaConfig quberaConfig) {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10000);
        requestFactory.setReadTimeout(30000);
        this.template = new RestTemplate(requestFactory);

        this.uriApi = quberaConfig.getKycUrl();
        this.apiKey = quberaConfig.getKycApiKey();
    }

    public AccountQuberaResponseDto createAccount(AccountQuberaRequestDto accountQuberaRequestDto) {
        String finalUrl = uriApi + "/account/create";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("apiKey", apiKey);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(finalUrl);
        URI uri = builder.build(true).toUri();

        HttpEntity<?> request = new HttpEntity<>(accountQuberaRequestDto, headers);

        ResponseEntity<AccountQuberaResponseDto> responseEntity =
                template.exchange(uri, HttpMethod.POST, request, AccountQuberaResponseDto.class);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            log.error("Error create account {}", responseEntity.getBody());
            throw new NgDashboardException("Error while creating account",
                    Constants.ErrorApi.QUBERA_CREATE_ACCOUNT_RESPONSE_ERROR);
        }

        return responseEntity.getBody();
    }
}
