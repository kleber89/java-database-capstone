package com.project.back_end.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private static final Logger logger = LoggerFactory.getLogger(PrescriptionService.class);

    // Constructor Injection for dependencies
    @Autowired
    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    // Method to save a new prescription
    public ResponseEntity<Map<String, String>> savePrescription(Prescription prescription) {
        Map<String, String> response = new HashMap<>();
        try {
            // Check if a prescription already exists for the same appointment
            if (prescriptionRepository.existsByAppointmentId(prescription.getAppointmentId())) {
                response.put("message", "A prescription already exists for this appointment.");
                return ResponseEntity.badRequest().body(response); // 400 Bad Request
            }

            // Save the new prescription
            prescriptionRepository.save(prescription);
            response.put("message", "Prescription saved successfully.");
            return ResponseEntity.status(201).body(response); // 201 Created
        } catch (Exception e) {
            logger.error("Error saving the prescription: {}", e.getMessage());
            response.put("error", "Internal server error.");
            return ResponseEntity.status(500).body(response); // 500 Internal Server Error
        }
    }

    // Method to retrieve a prescription associated with a specific appointment
    public ResponseEntity<Map<String, Object>> getPrescription(Long appointmentId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Prescription prescription = prescriptionRepository.findByAppointmentId(appointmentId);
            if (prescription == null) {
                response.put("message", "No prescription found for this appointment.");
                return ResponseEntity.ok(response); // 200 OK
            }

            response.put("prescription", prescription);
            return ResponseEntity.ok(response); // 200 OK
        } catch (Exception e) {
            logger.error("Error retrieving the prescription: {}", e.getMessage());
            response.put("error", "Internal server error.");
            return ResponseEntity.status(500).body(response); // 500 Internal Server Error
        }
    }
}
