package com.project.back_end.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.back_end.services.DoctorService; // Assuming this service exists
import com.project.back_end.services.UserService; // Shared service for validation
import com.project.back_end.models.Doctor; // Assuming Doctor is a model class representing a doctor
import com.project.back_end.DTO.Login; // Assuming Login is a DTO for login credentials

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.path} + doctor") // Base URL path for all methods in this controller
public class DoctorController {

    private final DoctorService doctorService; // Service for doctor-related logic
    private final UserService userService; // Service for validation logic

    // Constructor injection to autowire the services
    @Autowired
    public DoctorController(DoctorService doctorService, UserService userService) {
        this.doctorService = doctorService;
        this.userService = userService;
    }

    // Method to check doctor's availability
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(@PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable String date,
            @PathVariable String token) {
        // Validate the token
        ResponseEntity<Map<String, String>> tokenValidation = userService.validateToken(token, user);
        if (tokenValidation.getStatusCode().value() != 200) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized")); // Unauthorized
        }

        // Fetch doctor's availability
        java.time.LocalDate localDate = java.time.LocalDate.parse(date);
        List<String> availability = doctorService.getDoctorAvailability(doctorId, localDate);
        return ResponseEntity.ok(Map.of("availability", availability)); // Return availability status
    }

    // Method to get a list of all doctors
    @GetMapping
    public ResponseEntity<List<Doctor>> getDoctors() {
        List<Doctor> doctors = doctorService.getDoctors(); // Fetch all doctors
        return ResponseEntity.ok(doctors); // Return list of doctors
    }

    // Method to register a new doctor
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> saveDoctor(@RequestBody Doctor doctor, @PathVariable String token) {
        // Validate the token for admin role
        ResponseEntity<Map<String, String>> tokenValidation = userService.validateToken(token, "admin");
        if (tokenValidation.getStatusCode().value() != 200) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized")); // Unauthorized
        }

        // Attempt to save the doctor
        try {
            doctorService.saveDoctor(doctor);
            return ResponseEntity.status(201).body(Map.of("message", "Doctor added to db")); // Created
        } catch (Exception e) {
            return ResponseEntity.status(409).body(Map.of("error", "Doctor already exists")); // Conflict
        }
    }

    // Method for doctor login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(@RequestBody Login login) {
        // Validate doctor's credentials
        ResponseEntity<Map<String, String>> response = doctorService.validateDoctor(login);
        return response; // Return login status and token
    }

    // Method to update doctor details
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateDoctor(@RequestBody Doctor doctor, @PathVariable String token) {
        // Validate the token for admin role
        ResponseEntity<Map<String, String>> tokenValidation = userService.validateToken(token, "admin");
        if (tokenValidation.getStatusCode().value() != 200) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized")); // Unauthorized
        }

        // Attempt to update the doctor
        int result = doctorService.updateDoctor(doctor);
        if (result == 1) {
            return ResponseEntity.ok(Map.of("message", "Doctor updated")); // OK
        } else {
            return ResponseEntity.status(404).body(Map.of("error", "Doctor not found")); // Not found
        }
    }

    // Method to delete a doctor by ID
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> deleteDoctor(@PathVariable Long id, @PathVariable String token) {
        // Validate the token for admin role
        ResponseEntity<Map<String, String>> tokenValidation = userService.validateToken(token, "admin");
        if (tokenValidation.getStatusCode().value() != 200) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized")); // Unauthorized
        }

        // Attempt to delete the doctor
        int result = doctorService.deleteDoctor(id);
        if (result == 1) {
            return ResponseEntity.ok(Map.of("message", "Doctor deleted successfully")); // OK
        } else {
            return ResponseEntity.status(404).body(Map.of("error", "Doctor not found with id")); // Not found
        }
    }

    // Method to filter doctors based on name, time, and specialty
    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<List<Doctor>> filter(@PathVariable String name,
            @PathVariable String time,
            @PathVariable String speciality) {
        Map<String, Object> result = doctorService.filterDoctorsByNameSpecilityandTime(name, speciality, time);
        @SuppressWarnings("unchecked")
        List<Doctor> filteredDoctors = (List<Doctor>) result.get("doctors");
        return ResponseEntity.ok(filteredDoctors != null ? filteredDoctors : List.of()); // Return filtered list of doctors
    }
}
