package com.mercadona.nuriabravo;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WorkerAssignmentE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void assignWorkerToSection_shouldReturn409_whenHoursExceeded() throws Exception {
        String token = obtainValidToken();

        String requestBody = """
                {"sectionId": 2, "hours": 5}
                """;

        mockMvc.perform(post("/api/workers/1/assignments")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(containsString("hoursExceeded")));
    }

    private String obtainValidToken() throws Exception {
        String loginBody = """
                {"username": "admin", "password": "admin123"}
                """;

        String response = mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return JsonPath.read(response, "$.token");
    }
}