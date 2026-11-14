package com.example;

import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
abstract class ApiTestSupport {

    private static final Pattern ID = Pattern.compile("\"id\":(\\d+)");

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected UserRepository userRepository;

    protected MockMvc mvc;

    @BeforeEach
    void setUpMockMvc() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    protected String unique(String prefix) {
        return prefix + UUID.randomUUID().toString().substring(0, 8);
    }

    protected String teacherJson(String username) {
        return "{\"username\":\"" + username + "\",\"password\":\"pass123\","
                + "\"fullName\":\"Dr. Test\",\"email\":\"t@uni.edu\",\"department\":\"CS\"}";
    }

    protected String studentJson(String username) {
        return "{\"username\":\"" + username + "\",\"password\":\"pass123\","
                + "\"fullName\":\"Sam Test\",\"email\":\"s@uni.edu\",\"program\":\"Software Eng\"}";
    }

    protected long createTeacher(String username) throws Exception {
        MvcResult r = mvc.perform(post("/api/teachers")
                        .with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherJson(username)))
                .andExpect(status().isCreated())
                .andReturn();
        return idOf(r);
    }

    protected long createStudent(String username) throws Exception {
        MvcResult r = mvc.perform(post("/api/students")
                        .with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson(username)))
                .andExpect(status().isCreated())
                .andReturn();
        return idOf(r);
    }

    protected long idOf(MvcResult result) throws Exception {
        Matcher m = ID.matcher(result.getResponse().getContentAsString());
        assertTrue(m.find(), "response should contain an id");
        return Long.parseLong(m.group(1));
    }
}