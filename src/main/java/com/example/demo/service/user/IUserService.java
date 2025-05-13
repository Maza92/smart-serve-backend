package com.example.demo.service.user;

import java.util.List;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.user.UserDto;
import com.example.demo.entity.UserEntity;

public interface IUserService {
    List<UserEntity> getAllUsers();
    ApiSuccessDto<PageDto<UserDto>> getAllUsers(int page, int size);
}