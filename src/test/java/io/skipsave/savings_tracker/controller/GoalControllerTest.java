package io.skipsave.savings_tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.skipsave.savings_tracker.dto.GoalDTO;
import io.skipsave.savings_tracker.entity.Goal;
import io.skipsave.savings_tracker.mapper.GoalMapper;
import io.skipsave.savings_tracker.service.GoalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GoalController.class)
@ContextConfiguration(classes = {GoalController.class, GoalControllerTest.GoalServiceTestConfig.class})
class GoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GoalService goalService;

    private Goal goal;
    private GoalDTO goalDTO;


    @TestConfiguration
    static class GoalServiceTestConfig {
        @Bean
        public GoalService goalService() {
            return Mockito.mock(GoalService.class);
        }
    }

    @BeforeEach
    void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        goal = new Goal();
        goal.setId(1L);
        goal.setTitle("Test Goal");
        goal.setTargetAmount(100.0);
        goal.setSavedAmount(10.0);

        goalDTO = GoalMapper.toDTO(goal);
    }

    @Test
    void getGoalById_ShouldReturnGoalDTO() throws Exception {
        when(goalService.getGoalById(1L)).thenReturn(Optional.of(goal));

        mockMvc.perform(get("/api/goals/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Goal"));
    }


    @Test
    void getGoalById_WhenNotFound_ShouldReturn404() throws Exception {
        when(goalService.getGoalById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/goals/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createGoal_ShouldReturnCreatedGoalDTO() throws Exception {
        when(goalService.createGoal(any(Goal.class))).thenReturn(goal);

        mockMvc.perform(post("/api/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(goalDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Goal"));
    }


    @Test
    void updateGoal_WhenExists_ShouldReturnUpdatedGoalDTO() throws Exception {
        when(goalService.existsById(1L)).thenReturn(true);
        when(goalService.updateGoal(anyLong(), any(Goal.class))).thenReturn(goal);

        mockMvc.perform(put("/api/goals/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(goalDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Goal"));
    }




    @Test
    void deleteGoal_WhenExists_ShouldReturnNoContent() throws Exception {
        when(goalService.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/goals/1"))
                .andExpect(status().isNoContent());
    }
}
