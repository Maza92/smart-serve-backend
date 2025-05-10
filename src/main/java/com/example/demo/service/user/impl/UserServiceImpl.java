package com.example.demo.service.user.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.user.IUserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    
    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
}