package com.example.demo.controller.user.impl;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.controller.user.IUserController;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.data.ImportResultDto;
import com.example.demo.dto.user.UserDto;
import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.service.user.IUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements IUserController {

    private final IUserService userService;
    private final ApiExceptionFactory apiExceptionFactory;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiSuccessDto<PageDto<UserDto>>> getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String role,
            @RequestParam(defaultValue = "username") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        return ResponseEntity.ok(userService.getAllUsers(page, size, search, status, role, sortBy, sortDirection));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiSuccessDto<UserDto>> getUserById(int id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiSuccessDto<UserDto>> updateUser(int id,
            UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiSuccessDto<ImportResultDto>> importUsers(MultipartFile file) {
        try {
            return ResponseEntity.ok(userService.importUsers(file.getInputStream()));
        } catch (IOException e) {
            throw apiExceptionFactory.badRequestException("operation.user.import.invalid.file");
        }
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiSuccessDto<ImportResultDto>> importUsersAsync(MultipartFile file) {
        try {
            return ResponseEntity.ok(userService.importUsersAsync(file.getInputStream()).get());
        } catch (Exception e) {
            throw apiExceptionFactory.badRequestException("operation.user.import.invalid.file");
        }
    }
}
