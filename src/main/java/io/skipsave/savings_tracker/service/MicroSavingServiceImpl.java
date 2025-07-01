package io.skipsave.savings_tracker.service;

import io.skipsave.savings_tracker.entity.Goal;
import io.skipsave.savings_tracker.entity.MicroSaving;
import io.skipsave.savings_tracker.repository.GoalRepository;
import io.skipsave.savings_tracker.repository.MicroSavingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MicroSavingServiceImpl implements MicroSavingService {


    private final MicroSavingRepository microSavingRepository;
    private final GoalRepository goalRepository;


    public MicroSavingServiceImpl(MicroSavingRepository microSavingRepository, GoalRepository goalRepository) {
        this.microSavingRepository = microSavingRepository;
        this.goalRepository = goalRepository;
    }

    @Override
    public MicroSaving createMicroSaving(MicroSaving microSaving) {
        MicroSaving saved = microSavingRepository.save(microSaving);
        updateGoalSavedAmount(saved.getGoal());
        return saved;
    }

    @Override
    public List<MicroSaving> getMicroSavingsByGoal(Long goalId) {
        return microSavingRepository.findByGoalId(goalId);
    }

    @Override
    public Optional<MicroSaving> getMicroSavingById(Long id) {
        return microSavingRepository.findById(id);
    }

    @Override
    public MicroSaving updateMicroSaving(Long id, MicroSaving microSaving) {
        if (!microSavingRepository.existsById(id)) {
            throw new RuntimeException("MicroSaving not found with id: " + id);
        }
        microSaving.setId(id);
        MicroSaving updated = microSavingRepository.save(microSaving);
        updateGoalSavedAmount(updated.getGoal());
        return updated;
    }

    @Override
    public void deleteMicroSaving(Long id) {
        Optional<MicroSaving> msOpt = microSavingRepository.findById(id);
        if (msOpt.isEmpty()) {
            throw new RuntimeException("MicroSaving not found with id: " + id);
        }
        MicroSaving ms = msOpt.get();
        Goal goal = ms.getGoal();
        microSavingRepository.deleteById(id);
        updateGoalSavedAmount(goal);
    }

    private void updateGoalSavedAmount(Goal goal) {
        List<MicroSaving> allSavings = microSavingRepository.findByGoalId(goal.getId());
        double total = allSavings.stream().mapToDouble(MicroSaving::getAmount).sum();
        goal.setSavedAmount(total);
        goalRepository.save(goal);
    }


}