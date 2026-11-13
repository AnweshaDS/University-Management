package com.example.controller;

import com.example.dto.StudentRequest;
import com.example.dto.StudentResponse;
import com.example.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    public List<StudentResponse> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public StudentResponse one(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public StudentResponse create(@Valid @RequestBody StudentRequest req) {
        return service.create(req);
    }

    // admin: any student | student: only their own record
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and @ownership.isStudent(#id, authentication))")
    public StudentResponse update(@PathVariable Long id, @Valid @RequestBody StudentRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
