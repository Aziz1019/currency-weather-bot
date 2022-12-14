package com.example.cbu.helper;

import com.example.cbu.model.CurrencyDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CurrencyHelper {
    @Value("${currency.url}")
    private String url;
    private final RestTemplate restTemplate;
    public List<CurrencyDTO> getCurrencies() {
        ResponseEntity<List<CurrencyDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CurrencyDTO>>() {}
        );
        return response.getBody();
    }
}