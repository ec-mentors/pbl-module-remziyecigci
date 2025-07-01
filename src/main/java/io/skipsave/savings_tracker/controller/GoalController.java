package io.skipsave.savings_tracker.controller;

import io.skipsave.savings_tracker.dto.GoalDTO;
import io.skipsave.savings_tracker.entity.Goal;
import io.skipsave.savings_tracker.mapper.GoalMapper;
import io.skipsave.savings_tracker.service.GoalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }


    @GetMapping
    List<GoalDTO> getAllGoals() {
        List<Goal> goals = goalService.getAllGoals();
        return goals.stream()
                .map(GoalMapper::toDTO)
                .collect(Collectors.toList());
    }


    @GetMapping("/{id}")
    ResponseEntity<GoalDTO> getGoalById(@PathVariable Long id) {
        Optional<Goal> goalOpt = goalService.getGoalById(id);
        return goalOpt
                .map(goal -> ResponseEntity.ok(GoalMapper.toDTO(goal)))
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    ResponseEntity<GoalDTO> createGoal(@RequestBody GoalDTO goalDTO) {
        Goal goal = GoalMapper.toEntity(goalDTO);
        Goal created = goalService.createGoal(goal);
        return ResponseEntity.ok(GoalMapper.toDTO(created));
    }


    @PutMapping("/{id}")
    ResponseEntity<GoalDTO> updateGoal(@PathVariable Long id, @RequestBody GoalDTO goalDTO) {
        Goal goal = GoalMapper.toEntity(goalDTO);
        try {
            Goal updated = goalService.updateGoal(id, goal);
            return ResponseEntity.ok(GoalMapper.toDTO(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        try {
            goalService.deleteGoal(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
