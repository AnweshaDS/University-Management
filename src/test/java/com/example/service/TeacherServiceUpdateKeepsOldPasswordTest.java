package com.example.service;

import com.example.dto.TeacherRequest;
import com.example.model.AppUser;
import com.example.model.Role;
import com.example.model.Teacher;
import com.example.repository.TeacherRepository;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherServiceUpdateKeepsOldPasswordTest {

    @Mock
    private TeacherRepository teachers;

    @Mock
    private UserRepository users;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private TeacherService service;

    @Test
    void update_withBlankPassword_changesFieldsButKeepsOldHash() {
        Teacher existing = new Teacher();
        existing.setUser(new AppUser("t1", "OLDHASH", Role.TEACHER));
        existing.setFullName("Old Name");
        existing.setEmail("old@uni.edu");
        existing.setDepartment("CS");
        when(teachers.findById(1L)).thenReturn(Optional.of(existing));

        service.update(1L, new TeacherRequest(null, "", "New Name", "new@uni.edu", "Math"));

        assertEquals("New Name", existing.getFullName());
        assertEquals("Math", existing.getDepartment());
        assertEquals("OLDHASH", existing.getUser().getPassword());
        verify(encoder, never()).encode(any());
    }
}