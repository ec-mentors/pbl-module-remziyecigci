package io.skipsave.savings_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoalDTO {
    private Long id;
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotNull(message = "Target amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Target amount must be greater than 0")
    private Double targetAmount;

    @DecimalMin(value = "0.0", message = "Saved amount cannot be negative")
    private Double savedAmount;

    @Valid
    private List<MicroSavingDTO> microSavings = new ArrayList<>();

}


