package com.example.dto;

import com.example.model.Teacher;

public record TeacherResponse(Long id, String username, String fullName, String email, String department) {
    public static TeacherResponse from(Teacher t) {
        return new TeacherResponse(t.getId(), t.getUser().getUsername(), t.getFullName(), t.getEmail(), t.getDepartment());
    }
}
