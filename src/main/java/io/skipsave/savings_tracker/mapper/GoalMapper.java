package io.skipsave.savings_tracker.mapper;

import io.skipsave.savings_tracker.dto.GoalDTO;
import io.skipsave.savings_tracker.dto.MicroSavingDTO;
import io.skipsave.savings_tracker.entity.Goal;
import io.skipsave.savings_tracker.entity.MicroSaving;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GoalMapper {
    public static GoalDTO toDTO(Goal goal) {
        if (goal == null) return null;
        GoalDTO dto = new GoalDTO();
        dto.setId(goal.getId());
        dto.setTitle(goal.getTitle());
        dto.setTargetAmount(goal.getTargetAmount());
        dto.setSavedAmount(goal.getSavedAmount());

        List<MicroSavingDTO> microSavingDTOs = new java.util.ArrayList<>();
        if (goal.getMicroSavings() != null) {
            microSavingDTOs = goal.getMicroSavings().stream()
                    .filter(Objects::nonNull)
                    .map(MicroSavingMapper::toDTO)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        dto.setMicroSavings(microSavingDTOs);
        return dto;
    }

    public static Goal toEntity(GoalDTO dto) {
        if (dto == null) return null;
        Goal goal = new Goal();
        goal.setId(dto.getId());
        goal.setTitle(dto.getTitle());
        goal.setTargetAmount(dto.getTargetAmount());
        goal.setSavedAmount(dto.getSavedAmount());

        if (dto.getMicroSavings() != null) {
            List<MicroSaving> microSavings = dto.getMicroSavings().stream()
                    .filter(Objects::nonNull)
                    .map(msDto -> MicroSavingMapper.toEntity(msDto, goal))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            goal.setMicroSavings(microSavings);
        } else {
            goal.setMicroSavings(new ArrayList<>()); // NULL SAFE: boş liste
        }
        return goal;

    }
}
