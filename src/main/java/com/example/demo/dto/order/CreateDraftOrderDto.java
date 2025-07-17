package com.example.demo.dto.order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CreateDraftOrderDto {
    private Integer tableId;
    private Integer guestsCount;
}
