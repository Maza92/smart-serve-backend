package com.example.demo.dto.inventory.dashboard;

import java.time.Instant;

import com.example.demo.enums.UpdateTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class DashboardUpdateDto {
    private UpdateTypeEnum updateType;
    private Object data;
    private Instant timestamp;
}
