package com.example.demo.dto.restaurantTable;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Data
public class CreateRestaurantTableDto {
    @NotNull(message = "{validation.restaurant.table.number.notNull}")
    @Min(value = 1, message = "{validation.restaurant.table.number.min}")
    @Max(value = 999, message = "{validation.restaurant.table.number.max}")
    @Schema(example = "1")
    public Integer number;

    @NotNull(message = "{validation.restaurant.table.capacity.notNull}")
    @Min(value = 1, message = "{validation.restaurant.table.capacity.min}")
    @Max(value = 50, message = "{validation.restaurant.table.capacity.max}")
    @Schema(example = "4")
    public Integer capacity;

    @NotNull(message = "{validation.restaurant.table.status.notNull}")
    @NotBlank(message = "{validation.restaurant.table.status.notBlank}")
    @Size(min = 1, max = 20, message = "{validation.restaurant.table.status.size}")
    @Pattern(regexp = "^(AVAILABLE|OCCUPIED|RESERVED)$", message = "{validation.restaurant.table.status.pattern}")
    @Schema(example = "AVAILABLE")
    public String status;

    @Size(max = 50, message = "{validation.restaurant.table.section.size}")
    @Pattern(regexp = "^[a-zA-Z0-9\\s-]*$", message = "{validation.restaurant.table.section.pattern}")
    @Schema(example = "Terraza")
    public String section;
}
