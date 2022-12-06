package com.example.cbu.service;

import com.example.cbu.entity.UserEntity;

import java.util.Optional;

public interface UserService {
    Optional<UserEntity> findById(Long id);
    void save(UserEntity user);
}
