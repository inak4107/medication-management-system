
package com.medreminder.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.medreminder.app.repository.UserRepository;
import com.medreminder.app.model.User;
import com.medreminder.app.security.JwtService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

 private final UserRepository repo;
 private final PasswordEncoder encoder;
 private final JwtService jwt;

@PostMapping("/register")
public User register(@RequestBody User u){
    return repo.save(u);
}

 @PostMapping("/login")
 public String login(@RequestBody User u){
  User db = repo.findByEmail(u.getEmail()).orElseThrow();
  if(!encoder.matches(u.getPassword(), db.getPassword())) throw new RuntimeException();
  return jwt.generate(db.getEmail());
 }
}
