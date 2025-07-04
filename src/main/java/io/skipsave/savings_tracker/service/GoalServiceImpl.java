package io.skipsave.savings_tracker.service;

import io.skipsave.savings_tracker.entity.Goal;
import io.skipsave.savings_tracker.repository.GoalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;

    public GoalServiceImpl(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }


    @Override
    public Goal createGoal(Goal goal) {
        return goalRepository.save(goal);
    }

    @Override
    public List<Goal> getAllGoals() {
        return goalRepository.findAll();
    }

    @Override
    public Optional<Goal> getGoalById(Long id) {
        return goalRepository.findById(id);
    }

    @Override
    public Goal updateGoal(Long id, Goal goal) {
        if (!goalRepository.existsById(id)) {
            throw new RuntimeException("Goal not found with id: " + id);
        }
        goal.setId(id);
        return goalRepository.save(goal);
    }

    @Override
    public void deleteGoal(Long id) {
        if (!goalRepository.existsById(id)) {
            throw new RuntimeException("Goal not found with id: " + id);
        }
        goalRepository.deleteById(id);
    }
    @Override
    public boolean existsById(Long id) {
        return goalRepository.existsById(id);
    }
}