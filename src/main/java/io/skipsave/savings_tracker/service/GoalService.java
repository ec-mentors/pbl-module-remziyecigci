package io.skipsave.savings_tracker.service;


import io.skipsave.savings_tracker.entity.Goal;

import java.util.List;
import java.util.Optional;
public interface GoalService {
    Goal createGoal(Goal goal);
    List<Goal> getAllGoals();
    Optional<Goal> getGoalById(Long id);
    Goal updateGoal(Long id, Goal goal);
    void deleteGoal(Long id);
    boolean existsById(Long id);
}