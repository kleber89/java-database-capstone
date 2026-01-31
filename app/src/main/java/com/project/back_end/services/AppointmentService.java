package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.repo.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    @Autowired
    public AppointmentService(
            AppointmentRepository appointmentRepository,
            TokenService tokenService) {
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            if (appointment == null) {
                return 0;
            }
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional
    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {
        Map<String, String> response = new HashMap<>();
        if (appointment == null || appointment.getId() == null) {
            response.put("message", "Invalid appointment data.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Optional<Appointment> existingOpt = appointmentRepository.findById(appointment.getId());
        if (existingOpt.isEmpty()) {
            response.put("message", "Appointment not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        Appointment existing = existingOpt.get();
        if (!Objects.equals(existing.getPatient().getId(), appointment.getPatient().getId())) {
            response.put("message", "Unauthorized update attempt.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        // Aquí podrías agregar validaciones adicionales, por ejemplo, disponibilidad
        // del doctor
        try {
            appointmentRepository.save(appointment);
            response.put("message", "Appointment updated successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error updating appointment.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Transactional
    public ResponseEntity<Map<String, String>> cancelAppointment(long id, String token) {
        Map<String, String> response = new HashMap<>();
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
        if (appointmentOpt.isEmpty()) {
            response.put("message", "Appointment not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        Appointment appointment = appointmentOpt.get();
        Long patientId = tokenService.extractPatientId(token);
        if (patientId == null || appointment.getPatient() == null || 
            !Objects.equals(appointment.getPatient().getId(), patientId)) {
            response.put("message", "Unauthorized cancel attempt.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        try {
            appointmentRepository.delete(appointment);
            response.put("message", "Appointment canceled successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error canceling appointment.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getAppointment(String pname, LocalDate date, String token) {
        Map<String, Object> result = new HashMap<>();
        Long doctorId = tokenService.extractDoctorId(token);
        if (doctorId == null) {
            result.put("error", "Invalid or expired token.");
            return result;
        }
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<Appointment> appointments;
        if (pname != null && !pname.isEmpty()) {
            // Aquí deberías tener un método en el repositorio para filtrar por nombre de
            // paciente
            appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                    doctorId, start, end);
            appointments.removeIf(a -> !a.getPatient().getName().toLowerCase().contains(pname.toLowerCase()));
        } else {
            appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                    doctorId, start, end);
        }
        result.put("appointments", appointments);
        return result;
    }
}
