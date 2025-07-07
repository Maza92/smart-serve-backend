package com.example.demo.controller.inventory.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.inventory.IInventoryDashboardController;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.inventory.dashboard.InventoryDashboardDto;
import com.example.demo.service.data.CacheService;
import com.example.demo.service.inventory.IInventoryDashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryDashboardController implements IInventoryDashboardController {
    private final IInventoryDashboardService dashboardService;
    private final CacheService cacheService;

    public ResponseEntity<ApiSuccessDto<InventoryDashboardDto>> getDashboard() {
        return ResponseEntity.ok(dashboardService.getDashboardData());
    }

    public ResponseEntity<Void> refreshDashboard() {
        cacheService.clearInventoryDashboardCache();
        return ResponseEntity.ok().build();
    }
}
