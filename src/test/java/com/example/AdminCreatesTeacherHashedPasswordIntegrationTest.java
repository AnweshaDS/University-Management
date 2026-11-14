package com.example;

import com.example.model.AppUser;
import com.example.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminCreatesTeacherHashedPasswordIntegrationTest extends ApiTestSupport {

    @Test
    void adminCreatesTeacher_returns201_andPasswordIsStoredHashed() throws Exception {
        String username = unique("t");

        MvcResult result = mvc.perform(post("/api/teachers")
                        .with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherJson(username)))
                .andExpect(status().isCreated())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        assertTrue(json.contains("\"username\":\"" + username + "\""));
        assertFalse(json.contains("password"), "response must never contain the password");

        AppUser stored = userRepository.findByUsername(username).orElseThrow();
        assertNotEquals("pass123", stored.getPassword());
        assertTrue(stored.getPassword().startsWith("$2"), "password must be a BCrypt hash");
        assertEquals(Role.TEACHER, stored.getRole());
    }
}