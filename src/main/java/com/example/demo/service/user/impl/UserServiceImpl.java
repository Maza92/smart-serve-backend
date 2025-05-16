package com.example.demo.service.user.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.user.UserDto;
import com.example.demo.entity.RoleEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.enums.RoleEnum;
import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.mappers.IUserMapper;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.user.IUserService;
import com.example.demo.specifications.UserSpecifications;
import com.example.demo.utils.MessageUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final IUserMapper userMapper;
    private final MessageUtils messageUtils;
    private final ApiExceptionFactory apiExceptionFactory;

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public ApiSuccessDto<PageDto<UserDto>> getAllUsers(int page, int size, String search, String status, String role,
            String sortBy,
            String sortDirection) {

        if (size <= 0)
            throw apiExceptionFactory.badRequestException("operation.user.get.all.invalid.page.size");

        if (page <= 0)
            throw apiExceptionFactory.badRequestException("operation.user.get.all.invalid.page.number");

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, sort);

        Specification<UserEntity> spec = Specification.where(null);

        if (search != null && !search.isBlank()) {
            spec = spec.and(UserSpecifications.nameOrEmailContains(search));
        }

        if (status != null && !status.isBlank()) {
            spec = spec.and(UserSpecifications.activeEquals(status));
        }

        if (role != null && !role.isBlank()) {
            spec = spec.and(UserSpecifications.roleEquals(role));
        }

        Page<UserEntity> users = userRepository.findAll(spec, pageable);

        List<UserDto> usersDto = users.getContent().stream()
                .map(userMapper::toDto)
                .toList();

        System.out.println(usersDto);

        return ApiSuccessDto.of(HttpStatus.OK.value(), messageUtils.getMessage("operation.user.get.all.success"),
                PageDto.fromPage(users, usersDto));
    }

    @Override
    public ApiSuccessDto<UserDto> getUserById(int id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.user.get.by.id.not.found"));

        return ApiSuccessDto.of(HttpStatus.OK.value(), messageUtils.getMessage("operation.user.get.by.id.success"),
                userMapper.toDto(user));
    }

    @Override
    public ApiSuccessDto<UserDto> updateUser(int id, UserDto userDto) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.user.update.not.found"));

        userMapper.updateEntityFromDto(userDto, user);

        if (userDto.getRoleName() != null) {
            RoleEntity role = roleRepository.findByName(RoleEnum.valueOf(userDto.getRoleName().toUpperCase()))
                    .orElseThrow(() -> apiExceptionFactory.entityNotFound("role.not.found"));
            user.setRole(role);
        }

        userRepository.save(user);

        return ApiSuccessDto.of(HttpStatus.OK.value(), messageUtils.getMessage("operation.user.update.success"),
                userMapper.toDto(user));
    }
}