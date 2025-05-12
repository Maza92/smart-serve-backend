package com.example.demo.dto.cashRegister;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class PartialCreateCashRegisterDto {
    @NotNull(message = "{validation.cash.register.notes.notNull}")
    @NotBlank(message = "{validation.cash.register.notes.notBlank}")
    @Size(min = 1, max = 500, message = "{validation.cash.register.notes.size}")
    @Pattern(regexp = "^[a-zA-Z0-9\\s.,!?()-]*$", message = "{validation.cash.register.notes.pattern}")
    @Schema(example = "notes")
    public String notes;
}
