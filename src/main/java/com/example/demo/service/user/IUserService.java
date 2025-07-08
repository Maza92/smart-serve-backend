package com.example.demo.service.user;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.data.ImportResultDto;
import com.example.demo.dto.user.UserDto;
import com.example.demo.entity.UserEntity;

public interface IUserService {
    List<UserEntity> getAllUsers();

    ApiSuccessDto<PageDto<UserDto>> getAllUsers(int page, int size, String search, String status, String role,
            String sortBy,
            String sortDirection);

    ApiSuccessDto<UserDto> getUserById(int id);

    ApiSuccessDto<UserDto> updateUser(int id, UserDto userDto);

    ApiSuccessDto<ImportResultDto> importUsers(InputStream inputStream);

    ApiSuccessDto<UserDto> me();

    CompletableFuture<ApiSuccessDto<ImportResultDto>> importUsersAsync(InputStream inputStream);
}