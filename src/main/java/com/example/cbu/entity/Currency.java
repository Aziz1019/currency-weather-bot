package com.example.cbu.entity;

import lombok.*;
import org.springframework.web.client.RestTemplate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {
    @Id
    private Integer id;
    private Integer code;
    private String ccy;
    private String ccyNm_EN;
    private Integer nominal;
    private Double rate;
    private Double diff;
    private String date;


}
