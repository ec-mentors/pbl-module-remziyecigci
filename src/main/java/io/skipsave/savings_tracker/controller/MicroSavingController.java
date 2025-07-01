package io.skipsave.savings_tracker.controller;

import io.skipsave.savings_tracker.entity.MicroSaving;
import io.skipsave.savings_tracker.service.MicroSavingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/microsavings")
public class MicroSavingController {

    private final MicroSavingService microSavingService;

    public MicroSavingController(MicroSavingService microSavingService) {
        this.microSavingService = microSavingService;
    }

    @PostMapping("/{goalId}")
    public ResponseEntity<MicroSaving> createMicroSaving(@PathVariable Long goalId, @RequestBody MicroSaving microSaving) {
        return ResponseEntity.ok(microSavingService.createMicroSaving(microSaving));
    }

    @GetMapping("/goal/{goalId}")
    public ResponseEntity<List<MicroSaving>> getMicroSavingsByGoal(@PathVariable Long goalId) {
        return ResponseEntity.ok(microSavingService.getMicroSavingsByGoal(goalId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MicroSaving> getMicroSavingById(@PathVariable Long id) {
        return microSavingService.getMicroSavingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MicroSaving> updateMicroSaving(@PathVariable Long id, @RequestBody MicroSaving microSaving) {
        return ResponseEntity.ok(microSavingService.updateMicroSaving(id, microSaving));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMicroSaving(@PathVariable Long id) {
        microSavingService.deleteMicroSaving(id);
        return ResponseEntity.noContent().build();
    }
}