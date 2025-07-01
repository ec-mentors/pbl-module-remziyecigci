package io.skipsave.savings_tracker.controller;


import io.skipsave.savings_tracker.dto.MicroSavingDTO;
import io.skipsave.savings_tracker.entity.Goal;
import io.skipsave.savings_tracker.entity.MicroSaving;
import io.skipsave.savings_tracker.mapper.MicroSavingMapper;
import io.skipsave.savings_tracker.service.GoalService;
import io.skipsave.savings_tracker.service.MicroSavingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/microsavings")
public class MicroSavingController {


    private final MicroSavingService microSavingService;
    private final GoalService goalService;


    public MicroSavingController(MicroSavingService microSavingService, GoalService goalService) {
        this.microSavingService = microSavingService;
        this.goalService = goalService;
    }


    // not public methods
    @PostMapping("/{goalId}")
    ResponseEntity<MicroSavingDTO> createMicroSaving(@PathVariable Long goalId, @RequestBody MicroSavingDTO microSavingDTO) {
        Optional<Goal> goalOpt = goalService.getGoalById(goalId);
        if (goalOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        MicroSaving entity = MicroSavingMapper.toEntity(microSavingDTO, goalOpt.get());
        MicroSaving created = microSavingService.createMicroSaving(entity);
        return ResponseEntity.ok(MicroSavingMapper.toDTO(created));
    }

    @GetMapping("/goal/{goalId}")
    ResponseEntity<List<MicroSavingDTO>> getMicroSavingsByGoal(@PathVariable Long goalId) {
        List<MicroSaving> list = microSavingService.getMicroSavingsByGoal(goalId);
        if (list == null || list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<MicroSavingDTO> dtoList = list.stream()
                .map(MicroSavingMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    ResponseEntity<MicroSavingDTO> getMicroSavingById(@PathVariable Long id) {
        Optional<MicroSaving> msOpt = microSavingService.getMicroSavingById(id);
        return msOpt
                .map(MicroSavingMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    ResponseEntity<MicroSavingDTO> updateMicroSaving(@PathVariable Long id, @RequestBody MicroSavingDTO microSavingDTO) {
        Optional<Goal> goalOpt = goalService.getGoalById(microSavingDTO.getGoalId());
        if (goalOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        MicroSaving entity = MicroSavingMapper.toEntity(microSavingDTO, goalOpt.get());
        try {
            MicroSaving updated = microSavingService.updateMicroSaving(id, entity);
            return ResponseEntity.ok(MicroSavingMapper.toDTO(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteMicroSaving(@PathVariable Long id) {
        try {
            microSavingService.deleteMicroSaving(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}


