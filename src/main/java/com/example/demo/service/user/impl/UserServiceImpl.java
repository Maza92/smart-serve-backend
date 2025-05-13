package com.example.demo.service.user.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.user.UserDto;
import com.example.demo.entity.UserEntity;
import com.example.demo.mappers.IUserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.user.IUserService;
import com.example.demo.utils.MessageUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final IUserMapper userMapper;
    private final MessageUtils messageUtils;
    
    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Override
    public ApiSuccessDto<PageDto<UserDto>> getAllUsers(int page, int size) {

        Pageable pageable = Pageable.ofSize(size).withPage(page);

        Page<UserEntity> users = userRepository.findAll(pageable);

        List<UserDto> usersDto = users.getContent().stream()
                .map(userMapper::toDto)
                .toList();

        return  ApiSuccessDto.of(HttpStatus.OK.value(), messageUtils.getMessage("user.get.all.success"), PageDto.fromPage(users, usersDto));
    }
}