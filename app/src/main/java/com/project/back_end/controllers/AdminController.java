package com.project.back_end.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.back_end.services.UserService;
import com.project.back_end.models.Admin;

import java.util.Map;

@RestController
@RequestMapping("${api.path}/admin") // Ensure that api.path is defined in your properties file
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // Method to handle admin login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> adminLogin(@RequestBody Admin admin) {
        if (admin == null || admin.getUsername() == null || admin.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid admin credentials"));
        }

        try {
            ResponseEntity<Map<String, String>> response = userService.validateAdmin(admin);
            return response;
        } catch (Exception e) {
            // Log the error (you can use a logger here)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error during admin validation"));
        }
    }
}
