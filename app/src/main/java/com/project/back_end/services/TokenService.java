package com.project.back_end.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

@Component
public class TokenService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    private String jwtSecret = "your-secret-key"; // Should be from properties

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 7 days
                .signWith(getSigningKey())
                .compact();
    }

    public String extractSubject(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Map<String, Object> validateToken(String token, String role) {
        Map<String, Object> result = new HashMap<>();
        try {
            String subject = extractSubject(token);
            switch (role) {
                case "admin":
                    if (adminRepository.findByUsername(subject) == null) {
                        result.put("error", "Invalid admin token");
                    }
                    break;
                case "doctor":
                    if (doctorRepository.findByEmail(subject) == null) {
                        result.put("error", "Invalid doctor token");
                    }
                    break;
                case "patient":
                    if (patientRepository.findByEmail(subject) == null) {
                        result.put("error", "Invalid patient token");
                    }
                    break;
                default:
                    result.put("error", "Invalid role");
            }
        } catch (Exception e) {
            result.put("error", "Invalid token");
        }
        return result; // Empty if valid, has error if invalid
    }
}
