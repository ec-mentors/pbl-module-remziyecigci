package io.skipsave.savings_tracker.service;

import io.skipsave.savings_tracker.entity.MicroSaving;

import java.util.List;
import java.util.Optional;

public interface MicroSavingService {
    MicroSaving createMicroSaving(MicroSaving microSaving);
    List<MicroSaving> getMicroSavingsByGoal(Long goalId);
    Optional<MicroSaving> getMicroSavingById(Long id);
    MicroSaving updateMicroSaving(Long id, MicroSaving microSaving);
    void deleteMicroSaving(Long id);
}