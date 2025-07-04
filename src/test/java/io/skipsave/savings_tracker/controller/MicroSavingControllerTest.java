package io.skipsave.savings_tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.skipsave.savings_tracker.dto.MicroSavingDTO;
import io.skipsave.savings_tracker.entity.Goal;
import io.skipsave.savings_tracker.entity.MicroSaving;
import io.skipsave.savings_tracker.mapper.MicroSavingMapper;
import io.skipsave.savings_tracker.service.GoalService;
import io.skipsave.savings_tracker.service.MicroSavingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(MicroSavingController.class)
@ContextConfiguration(classes = {MicroSavingController.class, MicroSavingControllerTest.TestConfig.class})
class MicroSavingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MicroSavingService microSavingService;

    @Autowired
    private GoalService goalService;

    private MicroSaving microSaving;
    private MicroSavingDTO microSavingDTO;
    private Goal goal;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());



    @TestConfiguration
    static class TestConfig {
        @Bean
        public MicroSavingService microSavingService() {
            return Mockito.mock(MicroSavingService.class);
        }
        @Bean
        public GoalService goalService() {
            return Mockito.mock(GoalService.class);
        }
    }
    @BeforeEach
    void setup() {
        goal = new Goal();
        goal.setId(1L);
        goal.setTitle("Goal");


        microSaving = new MicroSaving();
        microSaving.setId(10L);
        microSaving.setAmount(50.0);
        microSaving.setDescription("Test");
        microSaving.setDate(LocalDate.now());
        microSaving.setGoal(goal);

        microSavingDTO = MicroSavingMapper.toDTO(microSaving);
    }

    @Test
    void createMicroSaving_ShouldReturnCreatedDTO() throws Exception {
        when(goalService.getGoalById(1L)).thenReturn(Optional.of(goal));
        when(microSavingService.createMicroSaving(any(MicroSaving.class))).thenReturn(microSaving);

        mockMvc.perform(post("/api/microsavings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(microSavingDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(50.0));
    }

    @Test
    void getMicroSavingsByGoal_ShouldReturnDTOList() throws Exception {
        when(microSavingService.getMicroSavingsByGoal(1L)).thenReturn(Collections.singletonList(microSaving));

        mockMvc.perform(get("/api/microsavings/goal/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10L));
    }

    @Test
    void getMicroSavingById_ShouldReturnDTO() throws Exception {
        when(microSavingService.getMicroSavingById(10L)).thenReturn(Optional.of(microSaving));

        mockMvc.perform(get("/api/microsavings/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L));
    }

    @Test
    void updateMicroSaving_ShouldReturnUpdatedDTO() throws Exception {
        when(goalService.getGoalById(anyLong())).thenReturn(Optional.of(goal));
        when(microSavingService.updateMicroSaving(anyLong(), any(MicroSaving.class))).thenReturn(microSaving);

        mockMvc.perform(put("/api/microsavings/10")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(microSavingDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L));
    }

    @Test
    void deleteMicroSaving_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/microsavings/10"))
                .andExpect(status().isNoContent());
    }
}