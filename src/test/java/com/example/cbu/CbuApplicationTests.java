package com.example.cbu;

import com.example.cbu.model.CurrencyDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class CbuApplicationTests {
    @Autowired
    private MockMvc mvc;
    private List<CurrencyDTO> currencies;

    @Test
    public void getCbuCurrencyByCodeTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/currency/cbu/840")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(200));
    }
    @Test
    public void getDummyCurrencyByCodeTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/currency/dummy/840")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(200));
    }
    @Test
    public void getCompositeCurrencyByCodeTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/currency/composite/840")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(200));
    }
    @Test
    public void getCurrencyByWrongCodeTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/currency/cbu/123")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(404));
    }
}