package com.example.cbu.service;

import com.example.cbu.entity.real.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findById(Long id);
    Boolean save(User user);
}
