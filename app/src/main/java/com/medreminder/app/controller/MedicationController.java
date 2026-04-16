package com.medreminder.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.medreminder.app.repository.MedicationRepository;
import com.medreminder.app.repository.UserRepository;
import com.medreminder.app.model.Medication;
import com.medreminder.app.model.User;

@RestController
@RequestMapping("/api/medications")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationRepository repo;
    private final UserRepository userRepo;

    @PostMapping("/{userId}")
    public Medication add(@PathVariable Long userId, @RequestBody Medication m) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        m.setUser(user);

        return repo.save(m);
    }

    @GetMapping("/{userId}")
    public List<Medication> get(@PathVariable Long userId) {

        return repo.findByUser_Id(userId);
    }
}