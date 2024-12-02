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
public class LoginResponse
{
    private Employeeresponse employee;
    private LocalDateTime loginTime;
    private String message;
    private String status;
    private String token;

}
