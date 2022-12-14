package com.example.cbu.helper;

import com.example.cbu.model.CurrencyDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CurrencyHelperTest {
    @Autowired
    private CurrencyHelper underTest;

    @Test
    void getSizeOfTheCurrenciesTest() {
        // given
        List<CurrencyDTO> currencies = underTest.getCurrencies();
        //then
        assertEquals(75, currencies.size());
    }

    @Test
    void getCodeOfTheCurrencyTest(){
        //given
        List<CurrencyDTO> currencies = underTest.getCurrencies();
        currencies.forEach(currencyDTO -> {
            if(currencyDTO.getCode().equals("840")) {
                //then
                assertEquals("840", currencyDTO.getCode());
            }
        });
    }

    @Test
    void getCcyOfTheCurrencyTest(){
        //given
        List<CurrencyDTO> currencies = underTest.getCurrencies();
        currencies.forEach(currencyDTO -> {
            if(currencyDTO.getCode().equals("840")) {
                //then
                assertEquals("USD", currencyDTO.getCcy());
            }
        });
    }

}