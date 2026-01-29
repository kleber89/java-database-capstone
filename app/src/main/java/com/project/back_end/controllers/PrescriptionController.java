package com.project.back_end.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.back_end.services.PrescriptionService; // Assuming this service exists
import com.project.back_end.services.UserService; // Shared service for validation
import com.project.back_end.services.AppointmentService; // Service for managing appointments
import com.project.back_end.models.Prescription; // Assuming Prescription is a model class representing a prescription

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
        if (!userService.validateToken(token, "doctor")) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized")); // Unauthorized
        }

        // Save the prescription and update the appointment status
        try {
            prescriptionService.savePrescription(prescription);
            appointmentService.updateAppointmentStatus(prescription.getAppointmentId(), "Prescription Issued");
            return ResponseEntity.status(201).body(Map.of("message", "Prescription saved successfully")); // Created
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to save prescription")); // Internal server
                                                                                                    // error
        }
    }

    // Method to get a prescription by appointment ID
    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<Map<String, Object>> getPrescription(@PathVariable Long appointmentId,
            @PathVariable String token) {
        // Validate the token for the "doctor" role
        if (!userService.validateToken(token, "doctor")) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized")); // Unauthorized
        }

        // Retrieve the prescription using the appointment ID
        Prescription prescription = prescriptionService.getPrescription(appointmentId);
        if (prescription != null) {
            return ResponseEntity.ok(Map.of("prescription", prescription)); // Return prescription details
        } else {
            return ResponseEntity.status(404).body(Map.of("error", "No prescription found for this appointment")); // Not
                                                                                                                   // found
        }
    }
}