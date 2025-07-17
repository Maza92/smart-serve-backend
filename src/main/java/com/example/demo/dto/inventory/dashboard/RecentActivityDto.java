package com.example.demo.dto.inventory.dashboard;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import com.example.demo.enums.ActivityTypeEnum;

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
public class RecentActivityDto {
    private String id;
    private ActivityTypeEnum activityType;
    private String description;
    private String userName;
    private Instant timestamp;
    private String timeAgo;
    private Map<String, Object> metadata;
}
