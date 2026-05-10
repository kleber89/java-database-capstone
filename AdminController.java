package com.project.back_end.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.project.back_end.services.UserService;
import com.project.back_end.models.Admin;
import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("${api.path}/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handle admin login request.
     * 
     * @param admin Admin credentials containing username and password
     * @return ResponseEntity with authentication response
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> adminLogin(@Valid @RequestBody Admin admin) {
        try {
            logger.info("Admin login attempt for username: {}", admin.getUsername());
            
            ResponseEntity<Map<String, String>> response = userService.validateAdmin(admin);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Admin login successful for username: {}", admin.getUsername());
            } else {
                logger.warn("Admin login failed for username: {}", admin.getUsername());
            }
            
            return response;
        } catch (IllegalArgumentException e) {
            logger.error("Invalid admin credentials provided", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid admin credentials"));
        } catch (Exception e) {
            logger.error("Unexpected error during admin validation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred during admin validation"));
        }
    }
}