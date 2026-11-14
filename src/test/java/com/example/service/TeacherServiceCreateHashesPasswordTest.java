package com.example.service;

import com.example.dto.TeacherRequest;
import com.example.dto.TeacherResponse;
import com.example.model.Role;
import com.example.model.Teacher;
import com.example.repository.TeacherRepository;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherServiceCreateHashesPasswordTest {

    @Mock
    private TeacherRepository teachers;

    @Mock
    private UserRepository users;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private TeacherService service;

    @Test
    void create_hashesPasswordAndSavesTeacherWithTeacherRole() {
        when(users.existsByUsername("t1")).thenReturn(false);
        when(encoder.encode("pass123")).thenReturn("HASHED");
        when(teachers.save(any(Teacher.class))).thenAnswer(inv -> inv.getArgument(0));

        TeacherResponse res = service.create(
                new TeacherRequest("t1", "pass123", "Dr. Ada", "ada@uni.edu", "CS"));

        ArgumentCaptor<Teacher> captor = ArgumentCaptor.forClass(Teacher.class);
        verify(teachers).save(captor.capture());
        Teacher saved = captor.getValue();

        assertEquals("HASHED", saved.getUser().getPassword());
        assertEquals(Role.TEACHER, saved.getUser().getRole());
        assertEquals("t1", res.username());
        assertEquals("CS", res.department());
    }
}