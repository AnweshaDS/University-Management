package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UnauthenticatedAndStudentForbiddenIntegrationTest extends ApiTestSupport {

    @Test
    void noLoginOrWrongPassword_returns401_andStudentCannotCreateOrDelete() throws Exception {
        long teacherId = createTeacher(unique("t"));
        String student = unique("s");
        createStudent(student);

        // not logged in
        mvc.perform(get("/api/teachers")).andExpect(status().isUnauthorized());

        // wrong password
        mvc.perform(get("/api/teachers").with(httpBasic("admin", "wrong")))
                .andExpect(status().isUnauthorized());

        // logged in as a student: only admin may create or delete
        mvc.perform(post("/api/teachers")
                        .with(httpBasic(student, "pass123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherJson(unique("t"))))
                .andExpect(status().isForbidden());

        mvc.perform(delete("/api/teachers/" + teacherId).with(httpBasic(student, "pass123")))
                .andExpect(status().isForbidden());
    }
}