package io.skipsave.savings_tracker.service;

import io.skipsave.savings_tracker.entity.Goal;
import io.skipsave.savings_tracker.entity.MicroSaving;
import io.skipsave.savings_tracker.repository.GoalRepository;
import io.skipsave.savings_tracker.repository.MicroSavingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MicroSavingServiceImplTest {

    @Mock
    private MicroSavingRepository microSavingRepository;

    @Mock
    private GoalRepository goalRepository;

    @InjectMocks
    private MicroSavingServiceImpl microSavingService;

    private Goal goal;
    private MicroSaving microSaving;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        goal = new Goal();
        goal.setId(1L);
        goal.setSavedAmount(0.0);

        microSaving = new MicroSaving();
        microSaving.setId(1L);
        microSaving.setAmount(100.0);
        microSaving.setGoal(goal);
    }

    @Test
    void createMicroSaving_ShouldSaveAndUpdateGoal() {
        when(microSavingRepository.save(any(MicroSaving.class))).thenReturn(microSaving);
        when(microSavingRepository.findByGoalId(goal.getId())).thenReturn(List.of(microSaving));
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);

        MicroSaving toSave = new MicroSaving();
        toSave.setAmount(100.0);
        toSave.setGoal(goal);

        MicroSaving result = microSavingService.createMicroSaving(toSave);

        assertNotNull(result);
        assertEquals(100.0, result.getAmount());
        verify(microSavingRepository, times(1)).save(any(MicroSaving.class));
        verify(goalRepository, times(1)).save(any(Goal.class));
        assertEquals(100.0, goal.getSavedAmount());
    }

    @Test
    void getMicroSavingsByGoal_ShouldReturnList() {
        when(microSavingRepository.findByGoalId(1L)).thenReturn(List.of(microSaving));

        List<MicroSaving> result = microSavingService.getMicroSavingsByGoal(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void getMicroSavingById_WhenExists_ShouldReturnMicroSaving() {
        when(microSavingRepository.findById(1L)).thenReturn(Optional.of(microSaving));

        Optional<MicroSaving> result = microSavingService.getMicroSavingById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void getMicroSavingById_WhenNotFound_ShouldReturnEmpty() {
        when(microSavingRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<MicroSaving> result = microSavingService.getMicroSavingById(99L);

        assertFalse(result.isPresent());
    }

    @Test
    void updateMicroSaving_WhenExists_ShouldUpdateAndReturnMicroSavingAndUpdateGoal() {
        MicroSaving updated = new MicroSaving();
        updated.setId(1L);
        updated.setAmount(150.0);
        updated.setGoal(goal);

        when(microSavingRepository.existsById(1L)).thenReturn(true);
        when(microSavingRepository.save(updated)).thenReturn(updated);
        when(microSavingRepository.findByGoalId(goal.getId())).thenReturn(List.of(updated));
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);

        MicroSaving result = microSavingService.updateMicroSaving(1L, updated);

        assertNotNull(result);
        assertEquals(150.0, result.getAmount());
        verify(goalRepository, times(1)).save(any(Goal.class));
        assertEquals(150.0, goal.getSavedAmount());
    }

    @Test
    void updateMicroSaving_WhenNotExists_ShouldThrowException() {
        MicroSaving ms = new MicroSaving();
        ms.setId(99L);
        ms.setGoal(goal);

        when(microSavingRepository.existsById(99L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            microSavingService.updateMicroSaving(99L, ms);
        });
        assertTrue(ex.getMessage().contains("MicroSaving not found"));
    }

    @Test
    void deleteMicroSaving_WhenExists_ShouldDeleteAndUpdateGoal() {
        when(microSavingRepository.findById(1L)).thenReturn(Optional.of(microSaving));
        doNothing().when(microSavingRepository).deleteById(1L);
        when(microSavingRepository.findByGoalId(goal.getId())).thenReturn(new ArrayList<>());
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);

        microSavingService.deleteMicroSaving(1L);

        verify(microSavingRepository, times(1)).deleteById(1L);
        verify(goalRepository, times(1)).save(any(Goal.class));
        assertEquals(0.0, goal.getSavedAmount());
    }

    @Test
    void deleteMicroSaving_WhenNotExists_ShouldThrowException() {
        when(microSavingRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            microSavingService.deleteMicroSaving(2L);
        });
        assertTrue(ex.getMessage().contains("MicroSaving not found"));
    }
}
