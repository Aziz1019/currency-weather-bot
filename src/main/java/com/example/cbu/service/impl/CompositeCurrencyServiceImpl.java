package com.example.cbu.service.impl;

import com.example.cbu.entity.Currency;
import com.example.cbu.responseModel.ResponseDto;
import com.example.cbu.service.CurrencyService;
import com.example.cbu.service.impl.enums.ServiceName;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CompositeCurrencyServiceImpl implements CurrencyService {
    private Integer id;
    private Integer currencyCode;
    private String ccy;
    private String ccyNm_EN;
    private Integer nominal;
    private String date;

    @Autowired
    private List<CurrencyService> services;


    @Override
    public ResponseDto findByCode(Integer code) {
        int counter = 0;
        Double rate = 1.0;
        Double diff = 1.0;

        for (CurrencyService service : this.services) {
            ResponseDto byCode = service.findByCode(code);
            Currency currency = (Currency) byCode.getData();
            if (currency.getId() > 0) {
                this.id = currency.getId();
                this.currencyCode = currency.getCode();
                this.ccy = currency.getCcy();
                this.ccyNm_EN = currency.getCcyNm_EN();
                this.nominal = currency.getNominal();
                rate += currency.getRate();
                diff += currency.getDiff();
                this.date = currency.getDate();
                counter++;
            } else {
                return ResponseDto.getSuccess(404, "Not found");
            }
        }

        return ResponseDto.getSuccess(new Currency(
                this.id,
                this.currencyCode,
                this.ccy,
                this.ccyNm_EN,
                this.nominal,
                (rate / counter),
                (diff / counter),
                this.date
        ));
    }

    @Override
    public ServiceName getStrategyName() {
        return ServiceName.COMPOSITE;
    }

    public void addService(CurrencyService c) {
        this.services.add(c);
    }

    public void removeService(CurrencyService c) {
        this.services.remove(c);
    }

    @Override
    public void save() {
    }

}



