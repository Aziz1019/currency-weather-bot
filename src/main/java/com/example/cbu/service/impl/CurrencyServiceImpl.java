package com.example.cbu.service.impl;

import com.example.cbu.dto.CurrencyDTO;
import com.example.cbu.entity.Currency;
import com.example.cbu.repository.CurrencyRepository;
import com.example.cbu.responseModel.ResponseDto;
import com.example.cbu.service.CurrencyService;
import com.example.cbu.service.impl.enums.ServiceName;
import com.example.cbu.util.CurrencyGetter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository repository;

    @Override
    public ResponseDto findByCode(Integer code) {
        Optional<Currency> byCode = repository.getByCode(code);
        return byCode.map(ResponseDto::getSuccess).orElseGet(() -> ResponseDto.getSuccess(404, "Not found"));
    }

    @Override
    public void save(){
        List<CurrencyDTO> currencies = CurrencyGetter.getCurrencies();
        for (CurrencyDTO currency : currencies) {
            repository.save(CurrencyDTO.toEntity(currency));
        }
    }
    @Override
    public ServiceName getStrategyName() {
        return ServiceName.CBU;
    }
}
