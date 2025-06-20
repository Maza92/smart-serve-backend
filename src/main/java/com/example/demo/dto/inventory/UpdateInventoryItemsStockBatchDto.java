package com.example.demo.dto.inventory;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class UpdateInventoryItemsStockBatchDto {
    @NotEmpty(message = "{validation.inventory.update.batch.not.found}")
    private List<UpdateInventoryItemStockDto> items;
}
