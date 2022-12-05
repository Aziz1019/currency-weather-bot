package com.example.cbu.service.impl;

import com.example.cbu.service.CurrencyService;
import com.example.cbu.service.impl.enums.ServiceName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class ServiceFactory {
    private Map<ServiceName, CurrencyService> services;

    @Autowired
    public ServiceFactory(Set<CurrencyService> currencyServices) {
        createService(currencyServices);
    }

    public CurrencyService findService(String serviceName) {
        ServiceName serviceName1 = ServiceName.valueOf(serviceName.toUpperCase());
        return services.get(serviceName1);
    }
    private void createService(Set<CurrencyService> currencyServices) {
        services = new HashMap<>();
        currencyServices.forEach(
                currencyService -> services.put(currencyService.getStrategyName(), currencyService));
    }
}