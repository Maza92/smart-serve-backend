package com.example.demo.service.shift.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.ShiftEntity;
import com.example.demo.repository.ShiftRepository;
import com.example.demo.service.shift.IShiftService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements IShiftService {

    private final ShiftRepository shiftRepository;
    
    @Override
    public List<ShiftEntity> getAllShifts() {
        return shiftRepository.findAll();
    }
}