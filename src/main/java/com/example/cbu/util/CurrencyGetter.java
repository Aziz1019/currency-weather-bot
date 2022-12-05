package com.example.cbu.util;

import com.example.cbu.dto.CurrencyDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RequiredArgsConstructor
public class CurrencyGetter {
    public static List<CurrencyDTO> getCurrencies() {
        RestTemplate restTemplate = new RestTemplate();
        List<CurrencyDTO> currencies = new ArrayList<>();

        String object = restTemplate.getForObject("https://cbu.uz/oz/arkhiv-kursov-valyut/json/", String.class);

        String substring1 = object.substring(object.indexOf("[") + 1, object.lastIndexOf("]"));
        String[] split = substring1.split("},");

        Map<Integer, List<String>> data = new HashMap<>();
        for (int i = 0; i < split.length; i++) {
            String[] split1 = split[i].split(",");
            List<String> list = new ArrayList<>(Arrays.asList(split1));
            data.put(i, list);
        }

        for (int i = 0; i < data.size(); i++) {
            CurrencyDTO currencyDTO = new CurrencyDTO();
            currencyDTO.setId(data.get(i).get(0).split(":")[1]);
            currencyDTO.setCode(data.get(i).get(1).split(":")[1]);
            currencyDTO.setCcy(data.get(i).get(2).split(":")[1]);
            currencyDTO.setCcyNm_EN(data.get(i).get(6).split(":")[1]);
            currencyDTO.setNominal(data.get(i).get(7).split(":")[1]);
            currencyDTO.setRate(data.get(i).get(8).split(":")[1]);
            currencyDTO.setDiff(data.get(i).get(9).split(":")[1]);
            currencyDTO.setDate(data.get(i).get(10).split(":")[1]);
            currencies.add(currencyDTO);
        }
        return currencies;
    }
}