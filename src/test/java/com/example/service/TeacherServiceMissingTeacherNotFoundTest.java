package com.example.service;

import com.example.exception.ResourceNotFoundException;
import com.example.repository.TeacherRepository;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherServiceMissingTeacherNotFoundTest {

    @Mock
    private TeacherRepository teachers;

    @Mock
    private UserRepository users;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private TeacherService service;

    @Test
    void findById_whenTeacherMissing_throwsResourceNotFound() {
        when(teachers.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }
}