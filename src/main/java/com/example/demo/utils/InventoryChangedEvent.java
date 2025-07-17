package com.example.demo.utils;

import org.springframework.context.ApplicationEvent;

import com.example.demo.enums.UpdateTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class InventoryChangedEvent extends ApplicationEvent {
    private final UpdateTypeEnum updateType;
    private final Object data;

    public InventoryChangedEvent(Object source, UpdateTypeEnum updateType, Object data) {
        super(source);
        this.updateType = updateType;
        this.data = data;
    }
}
