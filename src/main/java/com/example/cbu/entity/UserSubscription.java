package com.example.cbu.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
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

    public UserSubscription(Long userId, String firstName, String lastName, String username) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public UserSubscription(Boolean currencySubscription, Long userId, String firstName, String lastName, String username, String currencyCode) {
        this.currencySubscription = currencySubscription;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.currencyCode = currencyCode;
    }

//    public UserSubscription(Boolean currencySubscription, Long userId, String firstName, String lastName, String username, String currencyCode, String currencyTime) {
//        this.currencySubscription = currencySubscription;
//        this.userId = userId;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.username = username;
//        this.currencyCode = currencyCode;
//        this.currencyTime = currencyTime;
//    }

    public UserSubscription(Long userId, String firstName, String lastName, String username, String cityName, Boolean weatherSubscription) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.cityName = cityName;
        this.weatherSubscription = weatherSubscription;
    }
}
