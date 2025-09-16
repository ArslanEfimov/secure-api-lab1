package com.example.secure_api_lab1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthRequestDTO {


    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 20, message = "Username must be 3-20 characters long")
    @Pattern(regexp = "^[a-zA-Z]+[0-9]*$",
            message = "Username must start with letters, followed by numbers")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 3, message = "Password must be at least 6 characters")
    private String password;
}
