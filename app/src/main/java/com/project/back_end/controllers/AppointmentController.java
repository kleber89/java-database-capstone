package com.project.back_end.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.back_end.services.AppointmentService; // Assuming this service exists
import com.project.back_end.services.UserService; // Assuming this service handles validation logic
import com.project.back_end.models.Appointment; // Assuming Appointment is a model class representing an appointment

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
    public ResponseEntity<List<Appointment>> getAppointments(@PathVariable String date,
            @PathVariable String patientName,
            @PathVariable String token) {
        // Validate the token for role "doctor"
        if (!userService.validateToken(token, "doctor")) {
            return ResponseEntity.status(401).body(null); // Unauthorized
        }

        // Fetch appointments for the given patient on the specified date
        List<Appointment> appointments = appointmentService.getAppointments(date, patientName);
        return ResponseEntity.ok(appointments); // Return appointments
    }

    // Method to book a new appointment
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> bookAppointment(@RequestBody Appointment appointment,
            @PathVariable String token) {
        // Validate the token for role "patient"
        if (!userService.validateToken(token, "patient")) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized")); // Unauthorized
        }

        // Validate the appointment data
        String validationMessage = userService.validateAppointment(appointment);
        if (validationMessage != null) {
            return ResponseEntity.badRequest().body(Map.of("error", validationMessage)); // Bad request
        }

        // Book the appointment using the service
        boolean isBooked = appointmentService.bookAppointment(appointment);
        if (isBooked) {
            return ResponseEntity.status(201).body(Map.of("message", "Appointment booked successfully")); // Created
        } else {
            return ResponseEntity.status(400).body(Map.of("error", "Appointment slot is already taken")); // Bad request
        }
    }

    // Method to update an existing appointment
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateAppointment(@RequestBody Appointment appointment,
            @PathVariable String token) {
        // Validate the token for role "patient"
        if (!userService.validateToken(token, "patient")) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized")); // Unauthorized
        }

        // Update the appointment using the service
        boolean isUpdated = appointmentService.updateAppointment(appointment);
        if (isUpdated) {
            return ResponseEntity.ok(Map.of("message", "Appointment updated successfully")); // OK
        } else {
            return ResponseEntity.status(404).body(Map.of("error", "Appointment not found")); // Not found
        }
    }

    // Method to cancel a specific appointment
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> cancelAppointment(@PathVariable Long id,
            @PathVariable String token) {
        // Validate the token for role "patient"
        if (!userService.validateToken(token, "patient")) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized")); // Unauthorized
        }

        // Cancel the appointment using the service
        boolean isCancelled = appointmentService.cancelAppointment(id);
        if (isCancelled) {
            return ResponseEntity.ok(Map.of("message", "Appointment canceled successfully")); // OK
        } else {
            return ResponseEntity.status(404).body(Map.of("error", "Appointment not found")); // Not found
        }
    }
}
