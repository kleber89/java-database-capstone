package com.project.back_end.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.back_end.services.UserService; // Assuming the service class is named UserService
import com.project.back_end.models.Admin; // Assuming Admin is a model class representing admin credentials

import java.util.Map;

@RestController
@RequestMapping("${api.path}admin") // Base URL path for all endpoints in this controller
public class AdminController {

    private final UserService userService; // Service for handling business logic

    // Constructor injection to autowire the UserService
    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // Method to handle admin login
    @PostMapping("/login") // Endpoint for admin login
    public ResponseEntity<Map<String, String>> adminLogin(@RequestBody Admin admin) {
        // Call the validateAdmin method from UserService to perform login validation
        ResponseEntity<Map<String, String>> response = userService.validateAdmin(admin);
        return response; // Return the response from the service
    }
}
