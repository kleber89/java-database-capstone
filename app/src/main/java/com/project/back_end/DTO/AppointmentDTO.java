package com.project.back_end.DTO;

import com.project.back_end.models.Appointment;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Data Transfer Object (DTO) for Appointment data.
 * 
 * This DTO is used for communication between backend services and frontend
 * clients.
 * It simplifies and formats appointment data, decoupling it from internal
 * database models.
 * 
 * Note: This class contains NO persistence annotations like @Entity or @Id,
 * as it is meant purely for data transfer purposes.
 */
public class AppointmentDTO {

    // Core appointment fields
    private Long id;
    private Long doctorId;
    private String doctorName;
    private Long patientId;
    private String patientName;
    private String patientEmail;
    private String patientPhone;
    private String patientAddress;
    private LocalDateTime appointmentTime;
    private int status;

    // Derived fields (computed from appointmentTime)
    private LocalDate appointmentDate;
    private LocalTime appointmentTimeOnly;
    private LocalDateTime endTime;

    /**
     * Constructor that initializes all core fields and automatically computes
     * derived fields.
     * 
     * @param id              Unique identifier for the appointment
     * @param doctorId        ID of the doctor assigned to the appointment
     * @param doctorName      Full name of the doctor
     * @param patientId       ID of the patient
     * @param patientName     Full name of the patient
     * @param patientEmail    Email address of the patient
     * @param patientPhone    Contact number of the patient
     * @param patientAddress  Residential address of the patient
     * @param appointmentTime Full date and time of the appointment
     * @param status          Appointment status (e.g., 0 for Scheduled, 1 for
     *                        Completed)
     */
    public AppointmentDTO(Long id, Long doctorId, String doctorName, Long patientId,
            String patientName, String patientEmail, String patientPhone,
            String patientAddress, LocalDateTime appointmentTime, int status) {
        this.id = id;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.patientId = patientId;
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.patientPhone = patientPhone;
        this.patientAddress = patientAddress;
        this.appointmentTime = appointmentTime;
        this.status = status;

        // Automatically compute derived fields
        this.appointmentDate = appointmentTime.toLocalDate();
        this.appointmentTimeOnly = appointmentTime.toLocalTime();
        this.endTime = appointmentTime.plusHours(1);
    }

    // ============ Getter Methods ============

    public Long getId() {
        return id;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public Long getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public int getStatus() {
        return status;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public LocalTime getAppointmentTimeOnly() {
        return appointmentTimeOnly;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    // ============ Static Factory Method ============

    /**
     * Creates an AppointmentDTO from an Appointment entity.
     * 
     * @param appointment The Appointment entity to convert
     * @return A new AppointmentDTO instance with data from the Appointment
     */
    public static AppointmentDTO fromAppointment(Appointment appointment) {
        if (appointment == null) {
            return null;
        }
        return new AppointmentDTO(
                appointment.getId(),
                appointment.getDoctor() != null ? appointment.getDoctor().getId() : null,
                appointment.getDoctor() != null ? appointment.getDoctor().getName() : null,
                appointment.getPatient() != null ? appointment.getPatient().getId() : null,
                appointment.getPatient() != null ? appointment.getPatient().getName() : null,
                appointment.getPatient() != null ? appointment.getPatient().getEmail() : null,
                appointment.getPatient() != null ? appointment.getPatient().getPhone() : null,
                appointment.getPatient() != null ? appointment.getPatient().getAddress() : null,
                appointment.getAppointmentTime(),
                appointment.getStatus()
        );
    }
}
