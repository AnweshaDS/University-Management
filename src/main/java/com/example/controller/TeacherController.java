package com.example.controller;

import com.example.dto.TeacherRequest;
import com.example.dto.TeacherResponse;
import com.example.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService service;

    public TeacherController(TeacherService service) {
        this.service = service;
    }

    @GetMapping
    public List<TeacherResponse> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public TeacherResponse one(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public TeacherResponse create(@Valid @RequestBody TeacherRequest req) {
        return service.create(req);
    }

    // admin: any teacher | teacher: only their own record
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('TEACHER') and @ownership.isTeacher(#id, authentication))")
    public TeacherResponse update(@PathVariable Long id, @Valid @RequestBody TeacherRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
