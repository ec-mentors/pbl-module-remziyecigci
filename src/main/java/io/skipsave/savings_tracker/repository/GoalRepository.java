package io.skipsave.savings_tracker.repository;

import io.skipsave.savings_tracker.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<Goal, Long> {

}