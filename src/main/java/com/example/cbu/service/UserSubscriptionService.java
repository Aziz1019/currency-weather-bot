package com.example.cbu.service;

import com.example.cbu.entity.UserEntity;
import com.example.cbu.entity.UserSubscription;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface UserSubscriptionService {

    List<UserSubscription> findAllByCurrencySubscriptionIsTrue();

    List<UserSubscription> findAllByWeatherSubscriptionIsTrue();

    Optional<UserSubscription> findById(Long id);

    void save(UserSubscription user);
}
