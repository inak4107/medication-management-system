
package com.medreminder.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.*;
import com.medreminder.app.repository.*;
import com.medreminder.app.model.*;

@RestController
@RequestMapping("/api/medications")
@RequiredArgsConstructor
public class MedicationController {

 private final MedicationRepository repo;
 private final UserRepository userRepo;

 @PostMapping("/{userId}")
 public Medication add(@PathVariable Long userId, @RequestBody Medication m){
  User u = userRepo.findById(userId).orElseThrow();
  m.setUser(u);
  return repo.save(m);
 }

 @GetMapping("/{userId}")
 public List<Medication> get(@PathVariable Long userId){
  return repo.findByUserId(userId);
 }
}
