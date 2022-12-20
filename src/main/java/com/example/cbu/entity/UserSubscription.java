package com.example.cbu.entity;

import lombok.*;

import javax.persistence.Column;
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
    private String lastName;
    private String username;
    private String cityName;
    private String currencyCode;
    private String currencyTime;
    private String weatherTime;
    private Boolean currencySubscription = false;
    private Boolean weatherSubscription = false;



    public UserSubscription(Long userId, String firstName, String lastName, String username, String cityName, Boolean weatherSubscription) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.cityName = cityName;
        this.weatherSubscription = weatherSubscription;
    }
}
