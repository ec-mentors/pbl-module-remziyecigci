package io.skipsave.savings_tracker.integration;

import io.skipsave.savings_tracker.dto.GoalDTO;
import io.skipsave.savings_tracker.repository.GoalRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GoalIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GoalRepository goalRepository;

    @Test
    void shouldCreateAndFetchGoal() throws Exception {
        GoalDTO dto = new GoalDTO();
        dto.setTitle("Integration Test Goal");
        dto.setTargetAmount(400.0);
        dto.setSavedAmount(0.0);


        String response = mockMvc.perform(post("/api/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Integration Test Goal"))
                .andReturn().getResponse().getContentAsString();

        GoalDTO createdGoal = objectMapper.readValue(response, GoalDTO.class);
        assertThat(createdGoal.getId()).isNotNull();


        mockMvc.perform(get("/api/goals/" + createdGoal.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Integration Test Goal"));
    }

    @Test
    void shouldReturnBadRequestForInvalidGoal() throws Exception {
        GoalDTO dto = new GoalDTO();
        dto.setTargetAmount(100.0);


        mockMvc.perform(post("/api/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void fullCrudFlowForGoal() throws Exception {
        // CREATE
        GoalDTO dto = new GoalDTO();
        dto.setTitle("CRUD Goal");
        dto.setTargetAmount(400.0);
        dto.setSavedAmount(0.0);

        String response = mockMvc.perform(post("/api/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("CRUD Goal"))
                .andReturn().getResponse().getContentAsString();

        GoalDTO createdGoal = objectMapper.readValue(response, GoalDTO.class);
        Long goalId = createdGoal.getId();
        assertThat(goalId).isNotNull();

        // READ (Get by ID)
        mockMvc.perform(get("/api/goals/" + goalId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("CRUD Goal"));

        // READ (Get all)
        mockMvc.perform(get("/api/goals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("CRUD Goal"));

        // UPDATE
        createdGoal.setTitle("CRUD Goal Updated");
        createdGoal.setTargetAmount(999.0);

        mockMvc.perform(put("/api/goals/" + goalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdGoal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("CRUD Goal Updated"))
                .andExpect(jsonPath("$.targetAmount").value(999.0));

        // DELETE
        mockMvc.perform(delete("/api/goals/" + goalId))
                .andExpect(status().isNoContent());

        // GET after DELETE — Not Found
        mockMvc.perform(get("/api/goals/" + goalId))
                .andExpect(status().isNotFound());
    }
}