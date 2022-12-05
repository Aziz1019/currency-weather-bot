package com.example.cbu.service.impl;

import com.example.cbu.entity.Currency;
import com.example.cbu.repository.CurrencyRepository;
import com.example.cbu.responseModel.ResponseDto;
import com.example.cbu.service.CurrencyService;
import com.example.cbu.service.impl.enums.ServiceName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DummyCurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository repository;

    @Override
    public ResponseDto findByCode(Integer code) {
        Optional<Currency> cbu1 = repository.getByCode(code);
        if (cbu1.isPresent()) {
            Currency currency = new Currency();
            currency.setId(cbu1.get().getId());
            currency.setCode(cbu1.get().getCode());
            currency.setCcy(cbu1.get().getCcy());
            currency.setCcyNm_EN(cbu1.get().getCcyNm_EN());
            currency.setNominal(cbu1.get().getNominal());
            currency.setRate((cbu1.get().getRate() + 1989));
            currency.setDiff((cbu1.get().getDiff() + 7));
            currency.setDate(cbu1.get().getDate());
            return ResponseDto.getSuccess(currency);
        } else {
            return ResponseDto.getSuccess(404, "Not found");
        }
    }

    @Override
    public void save() {
    }

    @Override
    public ServiceName getStrategyName() {
        return ServiceName.DUMMY;
    }
}
