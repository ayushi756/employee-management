package com.example.employee_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employeeresponse
{
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String poistion;
    private double salary;
}
