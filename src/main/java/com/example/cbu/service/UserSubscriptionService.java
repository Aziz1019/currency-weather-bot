package com.example.cbu.service;

import com.example.cbu.entity.UserSubscription;

import java.util.List;
import java.util.Optional;

public interface UserSubscriptionService {

    List<UserSubscription> findAllByCurrencySubscriptionIsTrue();

    List<UserSubscription> findAllByWeatherSubscriptionIsTrue();

    Optional<UserSubscription> findById(Long id);

    Boolean save(UserSubscription user);
}
