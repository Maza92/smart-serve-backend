package com.example.demo.controller.Unit.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.Unit.IUnitController;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.unit.UnitDto;
import com.example.demo.service.unitConversion.IUnitConversionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/units")
public class UnitControllerImpl implements IUnitController {

    private final IUnitConversionService unitService;

    @Override
    public ResponseEntity<ApiSuccessDto<List<UnitDto>>> getUnits() {
        return ResponseEntity.ok(unitService.getUnits());
    }
}
