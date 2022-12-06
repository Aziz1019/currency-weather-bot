package com.example.cbu.service.impl;

import com.example.cbu.entity.UserEntity;
import com.example.cbu.repository.UserRepository;
import com.example.cbu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }
    public void save(UserEntity user){
       userRepository.save(user);
    }

}
