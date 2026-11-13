package com.example.security;

import com.example.repository.StudentRepository;
import com.example.repository.TeacherRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/** Used in @PreAuthorize expressions: is this record owned by the logged-in user? */
@Component("ownership")
public class Ownership {

    private final TeacherRepository teachers;
    private final StudentRepository students;

    public Ownership(TeacherRepository teachers, StudentRepository students) {
        this.teachers = teachers;
        this.students = students;
    }

    public boolean isTeacher(Long id, Authentication auth) {
        return teachers.findById(id)
                .map(t -> t.getUser().getUsername().equals(auth.getName()))
                .orElse(false);
    }

    public boolean isStudent(Long id, Authentication auth) {
        return students.findById(id)
                .map(s -> s.getUser().getUsername().equals(auth.getName()))
                .orElse(false);
    }
}
