package com.example.cbu.service.impl;

import com.example.cbu.entity.UserSubscription;
import com.example.cbu.repository.UserSubscriptionRepository;
import com.example.cbu.service.UserSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSubscriptionImpl implements UserSubscriptionService {
    private final UserSubscriptionRepository repository;

    @Override
    public List<UserSubscription> findAllByCurrencySubscriptionIsTrue() {
        return repository.findAllByCurrencySubscriptionIsTrue();
    }
    @Override
    public List<UserSubscription> findAllByWeatherSubscriptionIsTrue() {
        return repository.findAllByWeatherSubscriptionIsTrue();
    }
    @Override
    public Optional<UserSubscription> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Boolean save(UserSubscription user) {
        UserSubscription save = repository.save(user);
        return save.getUserId() != null;
    }
}
