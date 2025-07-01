package io.skipsave.savings_tracker.repository;


import io.skipsave.savings_tracker.entity.MicroSaving;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MicroSavingRepository extends JpaRepository<MicroSaving, Long> {

    List<MicroSaving> findByGoalId(Long goalId);
}