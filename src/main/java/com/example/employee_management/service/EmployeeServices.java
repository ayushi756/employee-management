package com.example.employee_management.service;

import com.example.employee_management.dto.EmployeeRequest;
import com.example.employee_management.dto.Employeeresponse;
import com.example.employee_management.dto.LoginRequest;
import com.example.employee_management.dto.LoginResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmployeeServices {
    Employeeresponse createEmployee(EmployeeRequest request);
    Employeeresponse updateEmployee(Long id, EmployeeRequest request);
    Employeeresponse getEmployeeById(Long id);
    List<Employeeresponse> getAllEmployees();
    void deleteEmployee(Long id);
    LoginResponse login (LoginRequest loginRequest) throws Exception;

    // Modified File
}
