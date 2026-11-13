package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/** username + password are required on create; on update only password is optional. */
public record TeacherRequest(
        String username,
        String password,
        @NotBlank String fullName,
        @NotBlank @Email String email,
        @NotBlank String department) {
}
