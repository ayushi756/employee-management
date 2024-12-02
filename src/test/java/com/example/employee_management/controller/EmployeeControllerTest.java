package com.example.employee_management.controller;

import com.example.employee_management.dto.EmployeeRequest;
import com.example.employee_management.dto.Employeeresponse;
import com.example.employee_management.dto.LoginRequest;
import com.example.employee_management.dto.LoginResponse;
import com.example.employee_management.service.EmployeeServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeServices employeeServices;

    @InjectMocks
    private EmployeeController employeeController;

    private EmployeeRequest employeeRequest;
    private Employeeresponse employeeResponse;
    private LoginRequest loginRequest;
    private LoginResponse loginResponse;

    @BeforeEach
    void setUp() {
        // Initialize test data
        employeeRequest = new EmployeeRequest();
        employeeRequest.setFirstName("John");
        employeeRequest.setLastName("Doe");
        employeeRequest.setEmail("john@example.com");
        employeeRequest.setPassword("password123");
        employeeRequest.setSalary(50000.0);
        employeeRequest.setDepartment("IT");
        employeeRequest.setPoistion("Developer");

        employeeResponse = new Employeeresponse();
        employeeResponse.setId(1L);
        employeeResponse.setFirstName("John");
        employeeResponse.setLastName("Doe");
        employeeResponse.setEmail("john@example.com");
        employeeResponse.setSalary(50000.0);
        employeeResponse.setDepartment("IT");
        employeeResponse.setPoistion("Developer");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("john@example.com");
        loginRequest.setPassword("password123");

        loginResponse = new LoginResponse();
        loginResponse.setMessage("Login successful");
        loginResponse.setEmployee(employeeResponse);
        loginResponse.setStatus("SUCCESS");
    }

    @Test
    void createEmployee_Success() {
        when(employeeServices.createEmployee(any(EmployeeRequest.class)))
                .thenReturn(employeeResponse);

        ResponseEntity<Employeeresponse> response = employeeController.createEmployee(employeeRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("john@example.com", response.getBody().getEmail());
        verify(employeeServices, times(1)).createEmployee(any(EmployeeRequest.class));
    }

    @Test
    void loginEmployee_Success() throws Exception {
        when(employeeServices.login(any(LoginRequest.class)))
                .thenReturn(loginResponse);

        ResponseEntity<?> response = employeeController.loginEmployee(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(employeeServices, times(1)).login(any(LoginRequest.class));
    }

    @Test
    void loginEmployee_Failure() throws Exception {
        when(employeeServices.login(any(LoginRequest.class)))
                .thenThrow(new Exception("Invalid credentials"));

        ResponseEntity<?> response = employeeController.loginEmployee(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    void getEmployeeById_Success() {
        when(employeeServices.getEmployeeById(1L))
                .thenReturn(employeeResponse);

        ResponseEntity<Employeeresponse> response = employeeController.getEmployeeById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(employeeServices, times(1)).getEmployeeById(1L);
    }

    @Test
    void getAllEmployees_Success() {
        List<Employeeresponse> employees = Arrays.asList(employeeResponse);
        when(employeeServices.getAllEmployees())
                .thenReturn(employees);

        ResponseEntity<List<Employeeresponse>> response = employeeController.getAllEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(employeeServices, times(1)).getAllEmployees();
    }

    @Test
    void updateEmployee_Success() {
        when(employeeServices.updateEmployee(anyLong(), any(EmployeeRequest.class)))
                .thenReturn(employeeResponse);

        ResponseEntity<Employeeresponse> response =
                employeeController.updateEmployee(1L, employeeRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("john@example.com", response.getBody().getEmail());
        verify(employeeServices, times(1)).updateEmployee(anyLong(), any(EmployeeRequest.class));
    }

    @Test
    void deleteEmployee_Success() {
        doNothing().when(employeeServices).deleteEmployee(anyLong());

        ResponseEntity<String> response = employeeController.deleteEmployee(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee deleted successfully", response.getBody());
        verify(employeeServices, times(1)).deleteEmployee(1L);
    }
}