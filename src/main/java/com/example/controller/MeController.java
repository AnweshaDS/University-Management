package com.example.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/me")
public class MeController {

    @GetMapping
    public Map<String, String> me(Authentication auth) {
        String role = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        return Map.of("username", auth.getName(), "role", role);
    }
}