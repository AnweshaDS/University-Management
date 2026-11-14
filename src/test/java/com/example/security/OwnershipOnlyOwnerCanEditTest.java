package com.example.security;

import com.example.model.AppUser;
import com.example.model.Role;
import com.example.model.Teacher;
import com.example.repository.StudentRepository;
import com.example.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OwnershipOnlyOwnerCanEditTest {

    @Mock
    private TeacherRepository teachers;

    @Mock
    private StudentRepository students;

    @InjectMocks
    private Ownership ownership;

    @Test
    void isTeacher_isTrueOnlyForTheAccountThatOwnsTheRecord() {
        Teacher teacher = new Teacher();
        teacher.setUser(new AppUser("t1", "hash", Role.TEACHER));
        when(teachers.findById(1L)).thenReturn(Optional.of(teacher));
        when(teachers.findById(2L)).thenReturn(Optional.empty());

        Authentication owner = UsernamePasswordAuthenticationToken.unauthenticated("t1", "x");
        Authentication other = UsernamePasswordAuthenticationToken.unauthenticated("t2", "x");

        assertTrue(ownership.isTeacher(1L, owner));
        assertFalse(ownership.isTeacher(1L, other));
        assertFalse(ownership.isTeacher(2L, owner));
    }
}