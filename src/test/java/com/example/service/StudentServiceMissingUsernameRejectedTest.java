package com.example.service;

import com.example.dto.StudentRequest;
import com.example.repository.StudentRepository;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class StudentServiceMissingUsernameRejectedTest {

    @Mock
    private StudentRepository students;

    @Mock
    private UserRepository users;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private StudentService service;

    @Test
    void create_withoutUsername_throwsAndTouchesNoDatabase() {
        StudentRequest req = new StudentRequest(null, "pass123", "Sam", "sam@uni.edu", "Software Eng");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.create(req));

        assertEquals("username and password are required", ex.getMessage());
        verifyNoInteractions(students);
    }
}