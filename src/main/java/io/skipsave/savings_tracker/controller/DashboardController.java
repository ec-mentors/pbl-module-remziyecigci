package io.skipsave.savings_tracker.controller;

import io.skipsave.savings_tracker.dto.GoalDTO;
import io.skipsave.savings_tracker.mapper.GoalMapper;
import io.skipsave.savings_tracker.service.GoalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final GoalService goalService;

    public DashboardController(GoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping("/")
    public String showDashboard(Model model) {
        List<GoalDTO> goals = goalService.getAllGoals()
                .stream()
                .map(GoalMapper::toDTO)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        double totalSavings = goals.stream()
                .filter(Objects::nonNull)
                .mapToDouble(g -> g.getSavedAmount() != null ? g.getSavedAmount() : 0)
                .sum();
        model.addAttribute("goals", goals != null ? goals : new ArrayList<>());
        model.addAttribute("totalSavings", totalSavings);
        return "dashboard";
    }
}
