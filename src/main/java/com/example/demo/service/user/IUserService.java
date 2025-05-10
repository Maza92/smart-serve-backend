package com.example.demo.service.user;

import java.util.List;

import com.example.demo.entity.UserEntity;

public interface IUserService {
    List<UserEntity> getAllUsers();
}