package com.example.secure_api_lab1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class AuthResponseDTO {

    private String token;
    private String username;
    private String message;
    private LocalDateTime timestamp;
}
