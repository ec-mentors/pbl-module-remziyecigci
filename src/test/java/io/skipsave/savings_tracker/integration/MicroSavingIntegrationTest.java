package io.skipsave.savings_tracker.integration;

import io.skipsave.savings_tracker.dto.GoalDTO;
import io.skipsave.savings_tracker.dto.MicroSavingDTO;
import io.skipsave.savings_tracker.repository.GoalRepository;
import io.skipsave.savings_tracker.repository.MicroSavingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MicroSavingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private MicroSavingRepository microSavingRepository;

    private Long goalId;

    @BeforeEach
    void setUp() throws Exception {
        GoalDTO goalDTO = new GoalDTO();
        goalDTO.setTitle("Goal for MicroSaving");
        goalDTO.setTargetAmount(500.0);
        goalDTO.setSavedAmount(0.0);

        String response = mockMvc.perform(post("/api/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(goalDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        GoalDTO createdGoal = objectMapper.readValue(response, GoalDTO.class);
        this.goalId = createdGoal.getId();
    }

    @Test
    void shouldCreateMicroSavingForGoal() throws Exception {
        MicroSavingDTO dto = new MicroSavingDTO();
        dto.setAmount(75.0);
        dto.setDescription("Test Saving");
        dto.setDate(LocalDate.now());
        dto.setGoalId(goalId);

        String response = mockMvc.perform(post("/api/microsavings/" + goalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(75.0))
                .andExpect(jsonPath("$.description").value("Test Saving"))
                .andReturn().getResponse().getContentAsString();

        MicroSavingDTO created = objectMapper.readValue(response, MicroSavingDTO.class);
        assertThat(created.getId()).isNotNull();
        assertThat(created.getGoalId()).isEqualTo(goalId);
    }

    @Test
    void shouldReturnBadRequestForInvalidMicroSaving() throws Exception {
        MicroSavingDTO dto = new MicroSavingDTO();
        dto.setDescription("No amount");
        dto.setDate(LocalDate.now());
        dto.setGoalId(goalId);

        mockMvc.perform(post("/api/microsavings/" + goalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void fullCrudFlowForMicroSaving() throws Exception {
        // CREATE
        MicroSavingDTO dto = new MicroSavingDTO();
        dto.setAmount(75.0);
        dto.setDescription("Test Saving");
        dto.setDate(LocalDate.now());
        dto.setGoalId(goalId);

        String response = mockMvc.perform(post("/api/microsavings/" + goalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(75.0))
                .andReturn().getResponse().getContentAsString();

        MicroSavingDTO created = objectMapper.readValue(response, MicroSavingDTO.class);
        Long microSavingId = created.getId();
        assertThat(microSavingId).isNotNull();

        // READ (Get by ID)
        mockMvc.perform(get("/api/microsavings/" + microSavingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(75.0));

        // READ (Get all by Goal)
        mockMvc.perform(get("/api/microsavings/goal/" + goalId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(75.0));

        // UPDATE
        created.setAmount(123.45);
        created.setDescription("Updated Description");

        mockMvc.perform(put("/api/microsavings/" + microSavingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(created)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(123.45))
                .andExpect(jsonPath("$.description").value("Updated Description"));

        // DELETE
        mockMvc.perform(delete("/api/microsavings/" + microSavingId))
                .andExpect(status().isNoContent());

        // GET after DELETE — Not Found
        mockMvc.perform(get("/api/microsavings/" + microSavingId))
                .andExpect(status().isNotFound());
    }
}