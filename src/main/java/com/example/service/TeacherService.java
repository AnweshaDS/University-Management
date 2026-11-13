package com.example.service;

import com.example.dto.TeacherRequest;
import com.example.dto.TeacherResponse;
import com.example.exception.ResourceNotFoundException;
import com.example.model.AppUser;
import com.example.model.Role;
import com.example.model.Teacher;
import com.example.repository.TeacherRepository;
import com.example.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeacherService {

    private final TeacherRepository teachers;
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public TeacherService(TeacherRepository teachers, UserRepository users, PasswordEncoder encoder) {
        this.teachers = teachers;
        this.users = users;
        this.encoder = encoder;
    }

    @Transactional(readOnly = true)
    public List<TeacherResponse> findAll() {
        return teachers.findAll().stream().map(TeacherResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public TeacherResponse findById(Long id) {
        return TeacherResponse.from(get(id));
    }

    @Transactional
    public TeacherResponse create(TeacherRequest req) {
        if (req.username() == null || req.username().isBlank()
                || req.password() == null || req.password().isBlank()) {
            throw new IllegalArgumentException("username and password are required");
        }
        if (users.existsByUsername(req.username())) {
            throw new IllegalArgumentException("username already exists");
        }
        Teacher t = new Teacher();
        t.setUser(new AppUser(req.username(), encoder.encode(req.password()), Role.TEACHER));
        apply(t, req);
        return TeacherResponse.from(teachers.save(t));
    }

    @Transactional
    public TeacherResponse update(Long id, TeacherRequest req) {
        Teacher t = get(id);
        apply(t, req);
        if (req.password() != null && !req.password().isBlank()) {
            t.getUser().setPassword(encoder.encode(req.password()));
        }
        return TeacherResponse.from(t);
    }

    @Transactional
    public void delete(Long id) {
        teachers.delete(get(id));
    }

    private void apply(Teacher t, TeacherRequest req) {
        t.setFullName(req.fullName());
        t.setEmail(req.email());
        t.setDepartment(req.department());
    }

    private Teacher get(Long id) {
        return teachers.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher " + id + " not found"));
    }
}
