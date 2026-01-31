package com.project.back_end.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.back_end.services.PrescriptionService; // Assuming this service exists
import com.project.back_end.services.UserService; // Shared service for validation
import com.project.back_end.services.AppointmentService; // Service for managing appointments
import com.project.back_end.models.Prescription; // Assuming Prescription is a model class representing a prescription

import java.util.Map;

@RestController
@RequestMapping("${api.path}prescription") // Base URL path for all methods in this controller
public class PrescriptionController {

    private final PrescriptionService prescriptionService; // Service for managing prescriptions
    private final UserService userService; // Service for validation logic
    private final AppointmentService appointmentService; // Service for managing appointments

    // Constructor injection to autowire the services
    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService,
            UserService userService,
            AppointmentService appointmentService) {
        this.prescriptionService = prescriptionService;
        this.userService = userService;
        this.appointmentService = appointmentService;
    }

    // Method to save a new prescription
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> savePrescription(@RequestBody Prescription prescription,
            @PathVariable String token) {
        // Validate the token for the "doctor" role
        ResponseEntity<Map<String, String>> tokenValidation = userService.validateToken(token, "doctor");
        if (tokenValidation.getStatusCode().value() != 200) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized")); // Unauthorized
        }

        // Save the prescription
        ResponseEntity<Map<String, String>> response = prescriptionService.savePrescription(prescription);
        if (response.getStatusCode().value() == 201) {
            // Note: updateAppointmentStatus method doesn't exist in AppointmentService
            // You may need to add this method or handle appointment status update differently
        }
        return response;
    }

    // Method to get a prescription by appointment ID
    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<Map<String, Object>> getPrescription(@PathVariable Long appointmentId,
            @PathVariable String token) {
        // Validate the token for the "doctor" role
        ResponseEntity<Map<String, String>> tokenValidation = userService.validateToken(token, "doctor");
        if (tokenValidation.getStatusCode().value() != 200) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized")); // Unauthorized
        }

        // Retrieve the prescription using the appointment ID
        ResponseEntity<Map<String, Object>> response = prescriptionService.getPrescription(appointmentId);
        return response; // Return prescription details
    }
}