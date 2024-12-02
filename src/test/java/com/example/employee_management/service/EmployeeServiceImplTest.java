package com.example.employee_management.service;

import com.example.employee_management.dto.EmployeeRequest;
import com.example.employee_management.dto.Employeeresponse;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.exception.EmailAlreadyExistsException;
import com.example.employee_management.exception.ResourceNotFoundException;
import com.example.employee_management.repository.EmployeeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepo employeeRepo;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeRequest employeeRequest;
    private Employeeresponse employeeResponse;

    @BeforeEach
    void setUp() {
        // Setup test data
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john@example.com");
        employee.setPassword("encodedPassword");
        employee.setDepartment("IT");
        employee.setSalary(50000.0);

        employeeRequest = new EmployeeRequest();
        employeeRequest.setFirstName("John");
        employeeRequest.setLastName("Doe");
        employeeRequest.setEmail("john@example.com");
        employeeRequest.setPassword("password123");
        employeeRequest.setDepartment("IT");
        employeeRequest.setSalary(50000.0);

        employeeResponse = new Employeeresponse();
        employeeResponse.setId(1L);
        employeeResponse.setFirstName("John");
        employeeResponse.setLastName("Doe");
        employeeResponse.setEmail("john@example.com");
        employeeResponse.setDepartment("IT");
        employeeResponse.setSalary(50000.0);
    }

    @Test
    void createEmployee_Success() {
        when(employeeRepo.existsByEmail(anyString())).thenReturn(false);
        when(employeeRepo.save(any(Employee.class))).thenReturn(employee);
        when(modelMapper.map(any(), eq(Employeeresponse.class))).thenReturn(employeeResponse);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        Employeeresponse result = employeeService.createEmployee(employeeRequest);

        assertNotNull(result);
        assertEquals(employeeResponse.getEmail(), result.getEmail());
        verify(employeeRepo, times(1)).save(any(Employee.class));
    }

    @Test
    void createEmployee_EmailExists() {
        when(employeeRepo.existsByEmail(anyString())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> {
            employeeService.createEmployee(employeeRequest);
        });

        verify(employeeRepo, never()).save(any(Employee.class));
    }

    @Test
    void getEmployeeById_Success() {
        when(employeeRepo.findById(1L)).thenReturn(Optional.of(employee));
        when(modelMapper.map(any(), eq(Employeeresponse.class))).thenReturn(employeeResponse);

        Employeeresponse result = employeeService.getEmployeeById(1L);

        assertNotNull(result);
        assertEquals(employeeResponse.getId(), result.getId());
        verify(employeeRepo, times(1)).findById(1L);
    }

    @Test
    void getEmployeeById_NotFound() {
        when(employeeRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeById(1L);
        });

        verify(employeeRepo, times(1)).findById(1L);
    }

    @Test
    void getAllEmployees_Success() {
        when(employeeRepo.findAll()).thenReturn(Arrays.asList(employee));
        when(modelMapper.map(any(), eq(Employeeresponse.class))).thenReturn(employeeResponse);

        List<Employeeresponse> results = employeeService.getAllEmployees();

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        verify(employeeRepo, times(1)).findAll();
    }

    @Test
    void deleteEmployee_Success() {
        when(employeeRepo.existsById(1L)).thenReturn(true);
        doNothing().when(employeeRepo).deleteById(1L);

        assertDoesNotThrow(() -> employeeService.deleteEmployee(1L));
        verify(employeeRepo, times(1)).deleteById(1L);
    }

    @Test
    void deleteEmployee_NotFound() {
        when(employeeRepo.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.deleteEmployee(1L);
        });

        verify(employeeRepo, never()).deleteById(anyLong());
    }
}