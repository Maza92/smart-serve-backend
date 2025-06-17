package com.example.demo.dto.user;

import com.example.demo.annotation.ImportColumn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserImportDto {
    @ImportColumn(name = "Username", required = true)
    private String username;

    @ImportColumn(name = "First name", required = true)
    private String firstName;

    @ImportColumn(name = "Last name", required = true)
    private String lastName;

    @ImportColumn(name = "Password", required = true)
    private String password;

    @ImportColumn(name = "Email", required = true)
    private String email;

    @ImportColumn(name = "Phone", required = false)
    private String phone;

    @ImportColumn(name = "Profile image path", required = false)
    private String profileImagePath;

    @ImportColumn(name = "Active", required = true)
    private boolean active;

    @ImportColumn(name = "Role ID", required = true)
    private int roleId;
}
