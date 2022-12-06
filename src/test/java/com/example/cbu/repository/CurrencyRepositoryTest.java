package com.example.cbu.repository;

import com.example.cbu.model.CurrencyDTO;
import com.example.cbu.entity.Currency;
import com.example.cbu.util.CurrencyGetter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CurrencyRepositoryTest {

    @Autowired
    private CurrencyRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldGetCurrencyByCodeTest() {
        //given
        List<CurrencyDTO> currencies = CurrencyGetter.getCurrencies();
        for (CurrencyDTO currency : currencies) {
            underTest.save(CurrencyDTO.toEntity(currency));
        }

        //when
        Integer code = 840;
        Optional<Currency> byCode = underTest.getByCode(code);

        //then
        assertTrue(byCode.isPresent());
    }

    @Test
    void itShouldNotGetCurrencyByCodeTest() {
        //given
        List<CurrencyDTO> currencies = CurrencyGetter.getCurrencies();
        for (CurrencyDTO currency : currencies) {
            underTest.save(CurrencyDTO.toEntity(currency));
        }

        //when
        Integer code = 120;
        Optional<Currency> byCode = underTest.getByCode(code);

        //then
        assertFalse(byCode.isPresent());
    }

}