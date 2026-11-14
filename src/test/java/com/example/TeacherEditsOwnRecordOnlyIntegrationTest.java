package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TeacherEditsOwnRecordOnlyIntegrationTest extends ApiTestSupport {

    @Test
    void teacherCanEditOwnRecord_butNotAStudentsRecord() throws Exception {
        String teacher = unique("t");
        String student = unique("s");
        long teacherId = createTeacher(teacher);
        long studentId = createStudent(student);

        // own record: allowed
        mvc.perform(put("/api/teachers/" + teacherId)
                        .with(httpBasic(teacher, "pass123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"Updated\",\"email\":\"t@uni.edu\",\"department\":\"Math\"}"))
                .andExpect(status().isOk());

        // someone else's record: forbidden
        mvc.perform(put("/api/students/" + studentId)
                        .with(httpBasic(teacher, "pass123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"Hacked\",\"email\":\"x@uni.edu\",\"program\":\"X\"}"))
                .andExpect(status().isForbidden());
    }
}