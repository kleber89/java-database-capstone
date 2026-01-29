package com.project.back_end.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.back_end.services.DoctorService; // Assuming this service exists
import com.project.back_end.services.UserService; // Shared service for validation
import com.project.back_end.models.Doctor; // Assuming Doctor is a model class representing a doctor
import com.project.back_end.models.Login; // Assuming Login is a DTO for login credentials

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
        if (!userService.validateToken(token, user)) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized")); // Unauthorized
        }

        // Fetch doctor's availability
        Map<String, Object> availability = doctorService.getDoctorAvailability(doctorId, date);
        return ResponseEntity.ok(availability); // Return availability status
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
        if (!userService.validateToken(token, "admin")) {
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
        Map<String, String> response = doctorService.validateDoctor(login);
        return ResponseEntity.ok(response); // Return login status and token
    }

    // Method to update doctor details
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateDoctor(@RequestBody Doctor doctor, @PathVariable String token) {
        // Validate the token for admin role
        if (!userService.validateToken(token, "admin")) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized")); // Unauthorized
        }

        // Attempt to update the doctor
        boolean updated = doctorService.updateDoctor(doctor);
        if (updated) {
            return ResponseEntity.ok(Map.of("message", "Doctor updated")); // OK
        } else {
            return ResponseEntity.status(404).body(Map.of("error", "Doctor not found")); // Not found
        }
    }

    // Method to delete a doctor by ID
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> deleteDoctor(@PathVariable Long id, @PathVariable String token) {
        // Validate the token for admin role
        if (!userService.validateToken(token, "admin")) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized")); // Unauthorized
        }

        // Attempt to delete the doctor
        boolean deleted = doctorService.deleteDoctor(id);
        if (deleted) {
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
        List<Doctor> filteredDoctors = doctorService.filterDoctors(name, time, speciality);
        return ResponseEntity.ok(filteredDoctors); // Return filtered list of doctors
    }
}
