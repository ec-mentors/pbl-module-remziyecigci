package io.skipsave.savings_tracker.controller;

import io.skipsave.savings_tracker.dto.GoalDTO;
import io.skipsave.savings_tracker.dto.MicroSavingDTO;
import io.skipsave.savings_tracker.mapper.GoalMapper;
import io.skipsave.savings_tracker.mapper.MicroSavingMapper;
import io.skipsave.savings_tracker.service.GoalService;
import io.skipsave.savings_tracker.service.MicroSavingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/goal")
public class GoalViewController {

    private final GoalService goalService;
    private final MicroSavingService microSavingService;

    public GoalViewController(GoalService goalService, MicroSavingService microSavingService) {
        this.goalService = goalService;
        this.microSavingService = microSavingService;
    }

    @GetMapping("/new")
    public String showGoalForm(Model model) {
        model.addAttribute("goal", new GoalDTO());
        model.addAttribute("formAction", "/goal");
        return "goal_form";
    }

    @PostMapping
    public String saveGoal(@ModelAttribute("goal") GoalDTO goalDTO) {
        goalService.createGoal(GoalMapper.toEntity(goalDTO));
        return "redirect:/";
    }


    @GetMapping("/edit/{id}")
    public String editGoal(@PathVariable Long id, Model model) {
        GoalDTO goalDTO = goalService.getGoalById(id)
                .map(GoalMapper::toDTO)
                .orElseThrow();
        model.addAttribute("goal", goalDTO);
        model.addAttribute("formAction", "/goal/edit/" + id);
        return "goal_form";
    }

    @PostMapping("/edit/{id}")
    public String updateGoal(@PathVariable Long id, @ModelAttribute("goal") GoalDTO goalDTO) {
        goalService.updateGoal(id, GoalMapper.toEntity(goalDTO));
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String confirmDeleteGoal(@PathVariable Long id, Model model) {
        GoalDTO goalDTO = goalService.getGoalById(id)
                .map(GoalMapper::toDTO)
                .orElseThrow();
        model.addAttribute("goal", goalDTO);
        return "goal_delete_confirm";
    }

    @PostMapping("/delete/{id}")
    public String deleteGoal(@PathVariable Long id) {
        goalService.deleteGoal(id);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String goalDetail(@PathVariable Long id, Model model) {

        GoalDTO goalDTO = goalService.getGoalById(id)
                .map(GoalMapper::toDTO)
                .orElseThrow();

        double progressPercent = 0;
        if (goalDTO.getSavedAmount() != null && goalDTO.getTargetAmount() != null && goalDTO.getTargetAmount() != 0) {
            progressPercent = (goalDTO.getSavedAmount() * 100.0) / goalDTO.getTargetAmount();
        }
        model.addAttribute("goal", goalDTO);
        model.addAttribute("progressPercent", progressPercent);



        List<MicroSavingDTO> microSavings = microSavingService.getMicroSavingsByGoal(id)
                .stream()
                .map(MicroSavingMapper::toDTO)
                .collect(Collectors.toList());
        model.addAttribute("goal", goalDTO);
        model.addAttribute("microSavings", microSavings);
        return "goal_detail";
    }
}
