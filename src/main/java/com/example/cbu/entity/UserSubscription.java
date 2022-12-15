package com.example.cbu.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
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
    private Boolean currencySubscription = false;
    private Boolean weatherSubscription = false;

    public UserSubscription(Long userId, String firstName, String lastName, String username) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public UserSubscription(Boolean currencySubscription, Long userId, String firstName, String lastName, String username) {
        this.currencySubscription = currencySubscription;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public UserSubscription(Long userId, String firstName, String lastName, String username, Boolean weatherSubscription) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.weatherSubscription = weatherSubscription;
    }
}
