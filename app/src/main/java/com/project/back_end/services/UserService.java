package com.project.back_end.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    // Constructor Injection for dependencies
    @Autowired
    public UserService(TokenService tokenService, AdminRepository adminRepository,
            DoctorRepository doctorRepository, PatientRepository patientRepository,
            DoctorService doctorService, PatientService patientService) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    // Method to validate the JWT token for a specific user
    public ResponseEntity<Map<String, String>> validateToken(String token, String user) {
        Map<String, String> response = new HashMap<>();
        if (!tokenService.validateToken(token, user)) {
            response.put("error", "Unauthorized: Invalid or expired token.");
            return ResponseEntity.status(401).body(response); // 401 Unauthorized
        }
        response.put("message", "Token is valid.");
        return ResponseEntity.ok(response); // 200 OK
    }

    // Method to validate the login credentials of an admin
    public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin) {
        Map<String, String> response = new HashMap<>();
        try {
            Admin admin = adminRepository.findByUsername(receivedAdmin.getUsername());
            if (admin != null && admin.getPassword().equals(receivedAdmin.getPassword())) {
                String token = tokenService.generateToken(admin.getUsername());
                response.put("token", token);
                return ResponseEntity.ok(response); // 200 OK
            } else {
                response.put("error", "Unauthorized: Invalid username or password.");
                return ResponseEntity.status(401).body(response); // 401 Unauthorized
            }
        } catch (Exception e) {
            logger.error("Error validating admin: {}", e.getMessage());
            response.put("error", "Internal server error.");
            return ResponseEntity.status(500).body(response); // 500 Internal Server Error
        }
    }

    // Method to filter doctors based on name, specialty, and available time
    public Map<String, Object> filterDoctor(String name, String specialty, String time) {
        return doctorService.filterDoctorsByNameSpecialtyAndTime(name, specialty, time);
    }

    // Method to validate whether an appointment is available based on the doctor's
    // schedule
    public int validateAppointment(Appointment appointment) {
        if (!doctorRepository.existsById(appointment.getDoctorId())) {
            return -1; // Doctor doesn't exist
        }
        return doctorService.getDoctorAvailability(appointment.getDoctorId(), appointment.getTime());
    }

    // Method to check whether a patient exists based on their email or phone number
    public boolean validatePatient(Patient patient) {
        return patientRepository.findByEmailOrPhone(patient.getEmail(), patient.getPhone()) == null;
    }

    // Method to validate a patient's login credentials (email and password)
    public ResponseEntity<Map<String, String>> validatePatientLogin(Login login) {
        Map<String, String> response = new HashMap<>();
        try {
            Patient patient = patientRepository.findByEmail(login.getEmail());
            if (patient != null && patient.getPassword().equals(login.getPassword())) {
                String token = tokenService.generateToken(patient.getEmail());
                response.put("token", token);
                return ResponseEntity.ok(response); // 200 OK
            } else {
                response.put("error", "Unauthorized: Invalid email or password.");
                return ResponseEntity.status(401).body(response); // 401 Unauthorized
            }
        } catch (Exception e) {
            logger.error("Error validating patient login: {}", e.getMessage());
            response.put("error", "Internal server error.");
            return ResponseEntity.status(500).body(response); // 500 Internal Server Error
        }
    }

    // Method to filter patient appointments based on condition and doctor name
    public ResponseEntity<Map<String, Object>> filterPatient(String condition, String name, String token) {
        String email = tokenService.extractEmail(token); // Extract email from token
        return patientService.filterByConditionAndDoctor(condition, name, email);
    }
}
