package com.example.demo.service.inventory;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.inventory.dashboard.DashboardUpdateDto;
import com.example.demo.dto.inventory.dashboard.InventoryDashboardDto;

public interface IInventoryDashboardService {

    public ApiSuccessDto<InventoryDashboardDto> getDashboardData();

    public void broadcastUpdate(DashboardUpdateDto update);

}
