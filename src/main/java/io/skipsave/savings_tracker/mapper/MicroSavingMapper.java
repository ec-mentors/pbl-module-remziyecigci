package io.skipsave.savings_tracker.mapper;

import io.skipsave.savings_tracker.dto.MicroSavingDTO;
import io.skipsave.savings_tracker.entity.Goal;
import io.skipsave.savings_tracker.entity.MicroSaving;

public class MicroSavingMapper {
    public static MicroSavingDTO toDTO(MicroSaving microSaving) {
        if (microSaving == null) return null;
        MicroSavingDTO dto = new MicroSavingDTO();
        dto.setId(microSaving.getId());
        dto.setAmount(microSaving.getAmount());
        dto.setDescription(microSaving.getDescription());
        dto.setDate(microSaving.getDate());
        dto.setGoalId(
                microSaving.getGoal() != null ? microSaving.getGoal().getId() : null
        );
        return dto;
    }

    public static MicroSaving toEntity(MicroSavingDTO dto, Goal goal) {
        if (dto == null) return null;
        MicroSaving ms = new MicroSaving();
        ms.setId(dto.getId());
        ms.setAmount(dto.getAmount());
        ms.setDescription(dto.getDescription());
        ms.setDate(dto.getDate());
        ms.setGoal(goal);
        return ms;
    }
}
