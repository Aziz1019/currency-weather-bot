package com.example.cbu.service.impl;

import com.example.cbu.dto.CurrencyDTO;
import com.example.cbu.entity.Currency;
import com.example.cbu.repository.CurrencyRepository;
import com.example.cbu.service.CurrencyService;
import com.example.cbu.service.impl.enums.ServiceName;
import com.example.cbu.util.CurrencyGetter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class CurrencyServiceImplTest {
    @Mock
    private CurrencyRepository repository;
    private CurrencyService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CurrencyServiceImpl(repository);
    }

    @Test
    void canGetFindByCodeTest() {
        // when
        Integer code = 840;
        underTest.findByCode(code);
        // then
        verify(repository).getByCode(code);
    }

    @Test
    void getStrategyName() {
        // when
        ServiceName strategyName = underTest.getStrategyName();
        // then
        assertThat(strategyName).isEqualTo(ServiceName.CBU);

    }
}

// This test method is not valid particularly for this service
// because it is not a business logic. It is just a method to save data;

// But I have written it to show how to test a method by saving data.
//    @Test
//    void saveTest() {
//        // given
//        Currency currency = new Currency(
//                5,
//                840,
//                "USD",
//                "US Dollar",
//                1,
//                1.1,
//                -0.5,
//                "Monday"
//        );
//        // when
//        underTest.save(currency);
//
//        // then
//        ArgumentCaptor<Currency> currencyArgumentCaptor =
//                ArgumentCaptor.forClass(Currency.class);
//        verify(repository).save(currencyArgumentCaptor.capture());
//        Currency value = currencyArgumentCaptor.getValue();
//        assertThat(value).isEqualTo(currency);
//    }
