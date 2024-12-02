package com.example.employee_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest
{
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private double salary;
    private String department;
    private String poistion;

}
