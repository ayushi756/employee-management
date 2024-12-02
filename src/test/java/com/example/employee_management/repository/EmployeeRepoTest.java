package com.example.employee_management.repository;

import com.example.employee_management.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class EmployeeRepoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepo employeeRepo;

    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setEmail("john@example.com");
        testEmployee.setPassword("password123");
        testEmployee.setDepartment("IT");
        testEmployee.setPosition("Developer"); // Make sure this matches your entity field name
        testEmployee.setSalary(50000.0);
    }

    @Test
    void findByEmail_Success() {
        // Save test employee
        Employee savedEmployee = entityManager.persist(testEmployee);
        entityManager.flush();

        // Test findByEmail
        Employee found = employeeRepo.findByEmail(savedEmployee.getEmail());

        assertNotNull(found);
        assertEquals(savedEmployee.getEmail(), found.getEmail());
    }

    @Test
    void findByEmail_NotFound() {
        Employee found = employeeRepo.findByEmail("nonexistent@example.com");
        assertNull(found);
    }

    @Test
    void existsByEmail_Success() {
        entityManager.persist(testEmployee);
        entityManager.flush();

        boolean exists = employeeRepo.existsByEmail(testEmployee.getEmail());
        assertTrue(exists);
    }

    @Test
    void existsByEmail_NotFound() {
        boolean exists = employeeRepo.existsByEmail("nonexistent@example.com");
        assertFalse(exists);
    }
}