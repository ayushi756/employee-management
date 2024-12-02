package com.example.employee_management.controller;

import com.example.employee_management.dto.*;
import com.example.employee_management.service.EmployeeServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j  // Lombok annotation for logging
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    @Autowired
    private EmployeeServices employeeServices;

    @PostMapping("/register")
    public ResponseEntity<Employeeresponse> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        log.info("Received request to create employee with email: {}", request.getEmail());
        Employeeresponse response = employeeServices.createEmployee(request);
        log.info("Successfully created employee with ID: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginEmployee(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        log.info("Login attempt for email: {}", loginRequest.getEmail());
        try {
            LoginResponse obj = employeeServices.login(loginRequest);
            log.info("Successful login for email: {}", loginRequest.getEmail());
            return ResponseEntity.ok(obj);
        } catch (Exception e) {
            log.error("Login failed for email: {}. Reason: {}", loginRequest.getEmail(), e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employeeresponse> getEmployeeById(@PathVariable Long id) {
        log.info("Fetching employee details for ID: {}", id);
        Employeeresponse response = employeeServices.getEmployeeById(id);
        log.info("Successfully retrieved employee details for ID: {}", id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Employeeresponse>> getAllEmployees() {
        log.info("Fetching all employees");
        List<Employeeresponse> employees = employeeServices.getAllEmployees();
        log.info("Successfully retrieved {} employees", employees.size());
        return ResponseEntity.ok(employees);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employeeresponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request) {
        log.info("Updating employee with ID: {}", id);
        Employeeresponse response = employeeServices.updateEmployee(id, request);
        log.info("Successfully updated employee with ID: {}", id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        log.info("Deleting employee with ID: {}", id);
        employeeServices.deleteEmployee(id);
        log.info("Successfully deleted employee with ID: {}", id);
        return ResponseEntity.ok("Employee deleted successfully");
    }
}