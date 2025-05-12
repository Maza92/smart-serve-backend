package com.example.demo.dto.cashRegister;

import java.math.BigDecimal;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Data
public class OpenCashRegisterDto {
    @NotNull(message = "{validation.cash.register.initial.amount.notNull}")
    @DecimalMin(value = "0.00", message = "{validation.cash.register.initial.amount.min}")
    @DecimalMax(value = "999999.99", message = "{validation.cash.register.initial.amount.max}")
    @Digits(integer = 6, fraction = 2, message = "{validation.cash.register.initial.amount.digits}")
    @PositiveOrZero(message = "{validation.cash.register.initial.amount.positiveOrZero}")
    @Schema(example = "100.00")
    public BigDecimal initialAmount;

    @NotNull(message = "{validation.cash.register.notes.notNull}")
    @NotBlank(message = "{validation.cash.register.notes.notBlank}")
    @Size(min = 1, max = 500, message = "{validation.cash.register.notes.size}")
    @Pattern(regexp = "^[a-zA-Z0-9\\s.,!?()-]*$", message = "{validation.cash.register.notes.pattern}")
    @Schema(example = "notes")
    public String notes;
}
