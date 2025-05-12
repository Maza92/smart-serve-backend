package com.example.demo.dto.cashRegister;

import java.math.BigDecimal;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Data
public class ClosedCashRegisterDto {
    @NotNull(message = "{validation.cash.register.final.amount.notNull}")
    @DecimalMin(value = "0.00", message = "{validation.cash.register.final.amount.min}")
    @DecimalMax(value = "999999.99", message = "{validation.cash.register.final.amount.max}")
    @Digits(integer = 6, fraction = 2, message = "{validation.cash.register.final.amount.digits}")
    @PositiveOrZero(message = "{validation.cash.register.final.amount.positiveOrZero}")
    @Schema(example = "150.00")
    public BigDecimal finalAmount;

    @NotNull(message = "{validation.cash.register.notes.notNull}")
    @NotBlank(message = "{validation.cash.register.notes.notBlank}")
    @Size(min = 1, max = 500, message = "{validation.cash.register.notes.size}")
    @Pattern(regexp = "^[a-zA-Z0-9\\s.,!?()-]*$", message = "{validation.cash.register.notes.pattern}")
    @Schema(example = "notes")
    public String notes;
}
