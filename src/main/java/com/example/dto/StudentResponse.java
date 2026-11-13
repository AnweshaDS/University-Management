package com.example.dto;

import com.example.model.Student;

public record StudentResponse(Long id, String username, String fullName, String email, String program) {
    public static StudentResponse from(Student s) {
        return new StudentResponse(s.getId(), s.getUser().getUsername(), s.getFullName(), s.getEmail(), s.getProgram());
    }
}
