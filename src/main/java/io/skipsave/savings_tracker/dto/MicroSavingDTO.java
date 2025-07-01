package io.skipsave.savings_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MicroSavingDTO {
    private Long id;
    private Double amount;
    private String description;
    private LocalDate date;
    private Long goalId;
}
