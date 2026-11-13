package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record StudentRequest(
        String username,
        String password,
        @NotBlank String fullName,
        @NotBlank @Email String email,
        @NotBlank String program) {
}
