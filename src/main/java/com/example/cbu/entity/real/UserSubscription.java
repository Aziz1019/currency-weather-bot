package com.example.cbu.entity.real;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSubscription {
    @Id
    private Long userId;
    private String firstName;
    private String username;
    private String cityName;
    private String currencyCode;
    private String currencyTime;
    private String weatherTime;
    private Boolean currencySubscription = false;
    private Boolean weatherSubscription = false;



    public UserSubscription(Long userId, String firstName, String username, String cityName, Boolean weatherSubscription) {
        this.userId = userId;
        this.firstName = firstName;
        this.username = username;
        this.cityName = cityName;
        this.weatherSubscription = weatherSubscription;
    }

    public UserSubscription(Boolean currencySubscription, Long userId, String firstName, String username, String countryCode) {
        this.currencySubscription = currencySubscription;
        this.userId = userId;
        this.firstName = firstName;
        this.username = username;
        this.currencyCode = countryCode;
    }
}
