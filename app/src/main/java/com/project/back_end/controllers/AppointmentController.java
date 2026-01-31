package com.project.back_end.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.back_end.services.AppointmentService; // Assuming this service exists
import com.project.back_end.services.UserService; // Assuming this service handles validation logic
import com.project.back_end.models.Appointment; // Assuming Appointment is a model class representing an appointment

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appointments") // Base URL path for all appointment-related endpoints
public class AppointmentController {

    private final AppointmentService appointmentService; // Service for appointment-related logic
    private final UserService userService; // Service for validation logic

    // Constructor injection to autowire the services
    @Autowired
    public AppointmentController(AppointmentService appointmentService, UserService userService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
    }

    // Method to get appointments based on date and patient name
    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<Map<String, Object>> getAppointments(@PathVariable String date,
            @PathVariable String patientName,
            @PathVariable String token) {
        // Validate the token for role "doctor"
        ResponseEntity<Map<String, String>> tokenValidation = userService.validateToken(token, "doctor");
        if (tokenValidation.getStatusCode().value() != 200) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized")); // Unauthorized
        }

        // Fetch appointments for the given patient on the specified date
        LocalDate localDate = LocalDate.parse(date);
        Map<String, Object> result = appointmentService.getAppointment(patientName, localDate, token);
        return ResponseEntity.ok(result); // Return appointments
    }

    // Method to book a new appointment
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> bookAppointment(@RequestBody Appointment appointment,
            @PathVariable String token) {
        // Validate the token for role "patient"
        ResponseEntity<Map<String, String>> tokenValidation = userService.validateToken(token, "patient");
        if (tokenValidation.getStatusCode().value() != 200) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized")); // Unauthorized
        }

        // Validate the appointment data
        int validationResult = userService.validateAppointment(appointment);
        if (validationResult == -1) {
            return ResponseEntity.badRequest().body(Map.of("error", "Doctor doesn't exist")); // Bad request
        } else if (validationResult == 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "Appointment slot is not available")); // Bad request
        }

        // Book the appointment using the service
        int result = appointmentService.bookAppointment(appointment);
        if (result == 1) {
            return ResponseEntity.status(201).body(Map.of("message", "Appointment booked successfully")); // Created
        } else {
            return ResponseEntity.status(400).body(Map.of("error", "Failed to book appointment")); // Bad request
        }
    }

    // Method to update an existing appointment
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateAppointment(@RequestBody Appointment appointment,
            @PathVariable String token) {
        // Validate the token for role "patient"
        ResponseEntity<Map<String, String>> tokenValidation = userService.validateToken(token, "patient");
        if (tokenValidation.getStatusCode().value() != 200) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized")); // Unauthorized
        }

        // Update the appointment using the service
        ResponseEntity<Map<String, String>> response = appointmentService.updateAppointment(appointment);
        return response; // Return the response from service
    }

    // Method to cancel a specific appointment
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> cancelAppointment(@PathVariable Long id,
            @PathVariable String token) {
        // Validate the token for role "patient"
        ResponseEntity<Map<String, String>> tokenValidation = userService.validateToken(token, "patient");
        if (tokenValidation.getStatusCode().value() != 200) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized")); // Unauthorized
        }

        // Cancel the appointment using the service
        ResponseEntity<Map<String, String>> response = appointmentService.cancelAppointment(id, token);
        return response; // Return the response from service
    }
}
