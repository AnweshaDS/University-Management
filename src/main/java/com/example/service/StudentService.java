package com.example.service;

import com.example.dto.StudentRequest;
import com.example.dto.StudentResponse;
import com.example.exception.ResourceNotFoundException;
import com.example.model.AppUser;
import com.example.model.Role;
import com.example.model.Student;
import com.example.repository.StudentRepository;
import com.example.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository students;
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public StudentService(StudentRepository students, UserRepository users, PasswordEncoder encoder) {
        this.students = students;
        this.users = users;
        this.encoder = encoder;
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> findAll() {
        return students.findAll().stream().map(StudentResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public StudentResponse findById(Long id) {
        return StudentResponse.from(get(id));
    }

    @Transactional
    public StudentResponse create(StudentRequest req) {
        if (req.username() == null || req.username().isBlank()
                || req.password() == null || req.password().isBlank()) {
            throw new IllegalArgumentException("username and password are required");
        }
        if (users.existsByUsername(req.username())) {
            throw new IllegalArgumentException("username already exists");
        }
        Student s = new Student();
        s.setUser(new AppUser(req.username(), encoder.encode(req.password()), Role.STUDENT));
        apply(s, req);
        return StudentResponse.from(students.save(s));
    }

    @Transactional
    public StudentResponse update(Long id, StudentRequest req) {
        Student s = get(id);
        apply(s, req);
        if (req.password() != null && !req.password().isBlank()) {
            s.getUser().setPassword(encoder.encode(req.password()));
        }
        return StudentResponse.from(s);
    }

    @Transactional
    public void delete(Long id) {
        students.delete(get(id));
    }

    private void apply(Student s, StudentRequest req) {
        s.setFullName(req.fullName());
        s.setEmail(req.email());
        s.setProgram(req.program());
    }

    private Student get(Long id) {
        return students.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student " + id + " not found"));
    }
}
