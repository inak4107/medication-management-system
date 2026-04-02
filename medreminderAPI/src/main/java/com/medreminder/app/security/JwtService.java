
package com.medreminder.app.security;

import io.jsonwebtoken.*;
import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class JwtService {
 private final String SECRET = "secret";

 public String generate(String email) {
  return Jwts.builder()
    .setSubject(email)
    .setIssuedAt(new Date())
    .setExpiration(new Date(System.currentTimeMillis()+86400000))
    .signWith(SignatureAlgorithm.HS256, SECRET)
    .compact();
 }

 public String extract(String token) {
  return Jwts.parser().setSigningKey(SECRET)
    .parseClaimsJws(token).getBody().getSubject();
 }
}
