package io.skipsave.savings_tracker.controller;

import io.skipsave.savings_tracker.dto.MicroSavingDTO;
import io.skipsave.savings_tracker.entity.MicroSaving;
import io.skipsave.savings_tracker.mapper.MicroSavingMapper;
import io.skipsave.savings_tracker.service.GoalService;
import io.skipsave.savings_tracker.service.MicroSavingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/goal/{goalId}/saving")
public class MicroSavingViewController {

    private final MicroSavingService microSavingService;
    private final GoalService goalService;

    public MicroSavingViewController(MicroSavingService microSavingService, GoalService goalService) {
        this.microSavingService = microSavingService;
        this.goalService = goalService;
    }


    @GetMapping("/new")
    public String showAddMicroSavingForm(@PathVariable Long goalId, Model model) {
        MicroSavingDTO microSaving = new MicroSavingDTO();
        microSaving.setDate(LocalDate.now());
        microSaving.setGoalId(goalId);
        model.addAttribute("microSaving", microSaving);
        model.addAttribute("goalId", goalId);

        String formAction = "/goal/" + goalId + "/saving";
        model.addAttribute("formAction", formAction);
        return "micro_saving_form";
    }

    @GetMapping("/edit/{microSavingId}")
    public String showEditMicroSavingForm(@PathVariable Long goalId, @PathVariable Long microSavingId, Model model) {
        MicroSaving microSavingEntity = microSavingService.getMicroSavingById(microSavingId)
                .orElseThrow(() -> new RuntimeException("Micro-saving not found"));
        MicroSavingDTO microSaving = MicroSavingMapper.toDTO(microSavingEntity);
        model.addAttribute("microSaving", microSaving);
        model.addAttribute("goalId", goalId);

        String formAction = "/goal/" + goalId + "/saving/edit/" + microSavingId;
        model.addAttribute("formAction", formAction);
        return "micro_saving_form";
    }


    @PostMapping
    public String saveMicroSaving(@PathVariable Long goalId, @ModelAttribute("microSaving") MicroSavingDTO microSavingDTO) {
        microSavingDTO.setGoalId(goalId);
        microSavingService.createMicroSaving(
                MicroSavingMapper.toEntity(microSavingDTO, goalService.getGoalById(goalId).orElseThrow())
        );
        return "redirect:/goal/" + goalId;
    }


    @PostMapping("/edit/{microSavingId}")
    public String updateMicroSaving(@PathVariable Long goalId, @PathVariable Long microSavingId,
                                    @ModelAttribute("microSaving") MicroSavingDTO microSavingDTO) {
        microSavingDTO.setGoalId(goalId);
        microSavingService.updateMicroSaving(
                microSavingId, MicroSavingMapper.toEntity(microSavingDTO, goalService.getGoalById(goalId).orElseThrow())
        );
        return "redirect:/goal/" + goalId;
    }

    @GetMapping("/delete/confirm/{microSavingId}")
    public String confirmDelete(@PathVariable Long goalId, @PathVariable Long microSavingId, Model model) {
        MicroSavingDTO microSaving = microSavingService.getMicroSavingById(microSavingId)
                .map(MicroSavingMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Micro-saving not found"));
        model.addAttribute("microSaving", microSaving);
        model.addAttribute("goalId", goalId);
        return "micro_saving_delete_confirm";
    }

    @PostMapping("/delete/confirm/{microSavingId}")
    public String deleteConfirmed(@PathVariable Long goalId, @PathVariable Long microSavingId) {
        microSavingService.deleteMicroSaving(microSavingId);
        return "redirect:/goal/" + goalId;
    }
}
