package com.example.cbu.service.impl;

import com.example.cbu.entity.real.User;
import com.example.cbu.service.UserService;
import com.example.cbu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    public Boolean save(User user){
        User save = userRepository.save(user);
        return save.getUserId() != null;
    }

}
