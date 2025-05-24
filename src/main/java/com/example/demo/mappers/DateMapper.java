package com.example.demo.mappers;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class DateMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a")
            .withZone(ZoneId.systemDefault());

    public String map(Instant instant) {
        if (instant == null)
            return null;
        return FORMATTER.format(instant);
    }
}
