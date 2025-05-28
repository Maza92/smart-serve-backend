package com.example.demo.dto.supplier;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class UpdateSupplierDto {
    @Size(max = 100, message = "{validation.supplier.name.size}")
    private String name;

    @Size(max = 100, message = "{validation.supplier.contactPerson.size}")
    private String contactPerson;

    @Pattern(regexp = "^[0-9+\\-\\s]*$", message = "{validation.supplier.phone.pattern}")
    @Size(max = 20, message = "{validation.supplier.phone.size}")
    private String phone;

    @Email(message = "{validation.supplier.email.valid}")
    @Size(max = 100, message = "{validation.supplier.email.size}")
    private String email;

    @Size(max = 255, message = "{validation.supplier.address.size}")
    private String address;

    private Boolean isActive;
}