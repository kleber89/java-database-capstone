package com.project.back_end.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.back_end.services.PatientService; // Assuming this service exists
import com.project.back_end.services.UserService; // Shared service for validation
import com.project.back_end.models.Patient; // Assuming Patient is a model class representing a patient
import com.project.back_end.models.Appointment;
import com.project.back_end.DTO.Login; // Assuming Login is a DTO for login credentials

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patient") // Base URL path for all methods in this controller
public class PatientController {

    private final PatientService patientService; // Service for patient-related logic
    private final UserService userService; // Service for validation logic

    // Constructor injection to autowire the services
    @Autowired
    public PatientController(PatientService patientService, UserService userService) {
        this.patientService = patientService;
        this.userService = userService;
    }

    // Method to get patient details
    @GetMapping("/{token}")
    public ResponseEntity<Map<String, Object>> getPatient(@PathVariable String token) {
        // Validate the token for the "patient" role
        ResponseEntity<Map<String, String>> tokenValidation = userService.validateToken(token, "patient");
        if (tokenValidation.getStatusCode().value() != 200) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized")); // Unauthorized
        }

        // Fetch patient details
        ResponseEntity<Map<String, Object>> patientDetails = patientService.getPatientDetails(token);
        return patientDetails; // Return patient details
    }

    // Method to create a new patient
    @PostMapping
    public ResponseEntity<Map<String, String>> createPatient(@RequestBody Patient patient) {
        // Validate if the patient already exists
        if (patientService.patientExists(patient)) {
            return ResponseEntity.status(409).body(Map.of("error", "Patient with email id or phone no already exists")); // Conflict
        }

        // Attempt to create the patient
        int result = patientService.createPatient(patient);
        if (result == 1) {
            return ResponseEntity.status(201).body(Map.of("message", "Signup successful")); // Created
        } else {
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error")); // Internal server error
        }
    }

    // Method for patient login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Login login) {
        // Validate patient's login credentials
        ResponseEntity<Map<String, String>> response = userService.validatePatientLogin(login);
        return response; // Return login status and token
    }

    // Method to get patient appointments
    @GetMapping("/{id}/{token}")
    public ResponseEntity<List<Appointment>> getPatientAppointments(@PathVariable Long id, @PathVariable String token) {
        // Validate the token for the "patient" role
        ResponseEntity<Map<String, String>> tokenValidation = userService.validateToken(token, "patient");
        if (tokenValidation.getStatusCode().value() != 200) {
            return ResponseEntity.status(401).body(null); // Unauthorized
        }

        // Fetch patient's appointments
        List<Appointment> appointments = patientService.getPatientAppointments(id);
        return ResponseEntity.ok(appointments); // Return patient's appointments
    }

    // Method to filter patient appointments
    @GetMapping("/filter/{condition}/{name}/{token}")
    public ResponseEntity<List<Appointment>> filterPatientAppointments(@PathVariable String condition,
            @PathVariable String name,
            @PathVariable String token) {
        // Validate the token for the "patient" role
        ResponseEntity<Map<String, String>> tokenValidation = userService.validateToken(token, "patient");
        if (tokenValidation.getStatusCode().value() != 200) {
            return ResponseEntity.status(401).body(null); // Unauthorized
        }

        // Filter appointments based on the given criteria
        List<Appointment> filteredAppointments = patientService.filterPatientAppointments(condition, name);
        return ResponseEntity.ok(filteredAppointments); // Return filtered appointments
    }
}
