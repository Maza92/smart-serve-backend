package com.example.demo.service.userShift.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.UserShiftEntity;
import com.example.demo.repository.UserShiftRepository;
import com.example.demo.service.userShift.IUserShiftService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserShiftServiceImpl implements IUserShiftService {

    private final UserShiftRepository userShiftRepository;
    
    @Override
    public List<UserShiftEntity> getAllUserShifts() {
        return userShiftRepository.findAll();
    }
}