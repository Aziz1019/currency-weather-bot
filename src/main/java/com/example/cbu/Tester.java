package com.example.cbu;


import com.example.cbu.model.CurrencyDTO;
import com.example.cbu.util.CurrencyGetter;

import java.util.List;

//Creating Tester class
public class Tester {
    public static void main(String[] args){
//        System.out.println(WeatherGetter.getWeather("Tashkent"));
        List<CurrencyDTO> currencies = CurrencyGetter.getCurrencies();

        for (CurrencyDTO currency : currencies) {
            System.out.println(currency);
        }
    }
}
