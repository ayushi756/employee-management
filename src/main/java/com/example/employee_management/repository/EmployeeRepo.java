package com.example.employee_management.repository;

import com.example.employee_management.entity.Employee;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepo extends JpaRepository<Employee, Long>
{
    Employee findByEmail(String email);
    boolean existsByEmail(String email);
}
