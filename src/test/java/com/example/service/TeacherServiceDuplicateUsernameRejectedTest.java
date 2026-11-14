package com.example.service;

import com.example.dto.TeacherRequest;
import com.example.model.Teacher;
import com.example.repository.TeacherRepository;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherServiceDuplicateUsernameRejectedTest {

    @Mock
    private TeacherRepository teachers;

    @Mock
    private UserRepository users;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private TeacherService service;

    @Test
    void create_withExistingUsername_throwsAndSavesNothing() {
        when(users.existsByUsername("t1")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                service.create(new TeacherRequest("t1", "pass123", "Dr. Ada", "ada@uni.edu", "CS")));

        verify(teachers, never()).save(any(Teacher.class));
    }
}