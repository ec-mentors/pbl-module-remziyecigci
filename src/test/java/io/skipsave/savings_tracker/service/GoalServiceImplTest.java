package io.skipsave.savings_tracker.service;

import io.skipsave.savings_tracker.entity.Goal;
import io.skipsave.savings_tracker.repository.GoalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GoalServiceImplTest {

    @Mock
    private GoalRepository goalRepository;

    @InjectMocks
    private GoalServiceImpl goalService;

    private Goal goal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        goal = new Goal();
        goal.setId(1L);
        goal.setTitle("Sample Goal");
        goal.setTargetAmount(100.0);
        goal.setSavedAmount(10.0);
    }

    @Test
    void createGoal_ShouldReturnSavedGoal() {
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);

        Goal toSave = new Goal();
        toSave.setTitle("Sample Goal");
        toSave.setTargetAmount(100.0);
        toSave.setSavedAmount(10.0);

        Goal result = goalService.createGoal(toSave);

        assertNotNull(result);
        assertEquals("Sample Goal", result.getTitle());
        verify(goalRepository, times(1)).save(any(Goal.class));
    }

    @Test
    void getAllGoals_ShouldReturnGoalList() {
        Goal goal2 = new Goal();
        goal2.setId(2L);

        List<Goal> list = Arrays.asList(goal, goal2);
        when(goalRepository.findAll()).thenReturn(list);

        List<Goal> result = goalService.getAllGoals();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void getGoalById_WhenExists_ShouldReturnGoal() {
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));

        Optional<Goal> result = goalService.getGoalById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void getGoalById_WhenNotFound_ShouldReturnEmpty() {
        when(goalRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Goal> result = goalService.getGoalById(99L);

        assertFalse(result.isPresent());
    }

    @Test
    void updateGoal_WhenExists_ShouldUpdateAndReturnGoal() {
        when(goalRepository.existsById(1L)).thenReturn(true);
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);

        Goal toUpdate = new Goal();
        toUpdate.setTitle("Updated");
        toUpdate.setTargetAmount(150.0);
        toUpdate.setSavedAmount(20.0);

        Goal result = goalService.updateGoal(1L, toUpdate);

        assertNotNull(result);
        verify(goalRepository, times(1)).save(any(Goal.class));
    }

    @Test
    void updateGoal_WhenNotExists_ShouldThrowException() {
        when(goalRepository.existsById(99L)).thenReturn(false);

        Goal toUpdate = new Goal();
        toUpdate.setTitle("Updated");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            goalService.updateGoal(99L, toUpdate);
        });

        assertTrue(ex.getMessage().contains("Goal not found"));
    }

    @Test
    void deleteGoal_WhenExists_ShouldCallDelete() {
        when(goalRepository.existsById(1L)).thenReturn(true);
        doNothing().when(goalRepository).deleteById(1L);

        goalService.deleteGoal(1L);

        verify(goalRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteGoal_WhenNotExists_ShouldThrowException() {
        when(goalRepository.existsById(99L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            goalService.deleteGoal(99L);
        });

        assertTrue(ex.getMessage().contains("Goal not found"));
    }

    @Test
    void existsById_ShouldReturnTrueOrFalse() {
        when(goalRepository.existsById(1L)).thenReturn(true);
        when(goalRepository.existsById(99L)).thenReturn(false);

        assertTrue(goalService.existsById(1L));
        assertFalse(goalService.existsById(99L));
    }
}
