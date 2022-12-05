package com.example.cbu.service;

import com.example.cbu.responseModel.ResponseDto;
import com.example.cbu.service.impl.enums.ServiceName;


public interface CurrencyService {
    ResponseDto findByCode(Integer code);
    void save();
    ServiceName getStrategyName();
}
