package io.skipsave.savings_tracker.mapper;

import io.skipsave.savings_tracker.dto.GoalDTO;
import io.skipsave.savings_tracker.dto.MicroSavingDTO;
import io.skipsave.savings_tracker.entity.Goal;

import java.util.List;
import java.util.stream.Collectors;

public class GoalMapper {
    public static GoalDTO toDTO(Goal goal) {
        GoalDTO dto = new GoalDTO();
        dto.setId(goal.getId());
        dto.setTitle(goal.getTitle());
        dto.setTargetAmount(goal.getTargetAmount());
        dto.setSavedAmount(goal.getSavedAmount());
        List<MicroSavingDTO> microSavingDTOs = goal.getMicroSavings()
                .stream()
                .map(MicroSavingMapper::toDTO)
                .collect(Collectors.toList());
        dto.setMicroSavings(microSavingDTOs);
        return dto;
    }

    public static Goal toEntity(GoalDTO dto) {
        Goal goal = new Goal();
        goal.setId(dto.getId());
        goal.setTitle(dto.getTitle());
        goal.setTargetAmount(dto.getTargetAmount());
        goal.setSavedAmount(dto.getSavedAmount());
        return goal;
    }
}
