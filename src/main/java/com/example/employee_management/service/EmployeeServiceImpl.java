package com.example.employee_management.service;

import com.example.employee_management.dto.EmployeeRequest;
import com.example.employee_management.dto.Employeeresponse;
import com.example.employee_management.dto.LoginRequest;
import com.example.employee_management.dto.LoginResponse;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.exception.EmailAlreadyExistsException;
import com.example.employee_management.exception.InvalidCredentialsException;
import com.example.employee_management.exception.ResourceNotFoundException;
import com.example.employee_management.repository.EmployeeRepo;
import com.example.employee_management.security.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeServices{

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private  JwtUtil jwtUtil;

    @Override
    public Employeeresponse createEmployee(EmployeeRequest request)
    {
        if(employeeRepo.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registerd" + request.getEmail());
        }

            Employee employee = new Employee();
            employee.setFirstName(request.getFirstName());
            employee.setLastName(request.getLastName());
            employee.setEmail(request.getEmail());
            employee.setPassword(passwordEncoder.encode(request.getPassword()));
            employee.setDepartment(request.getDepartment());
            employee.setSalary(request.getSalary());
            Employee register = employeeRepo.save(employee);
            return modelMapper.map(register, Employeeresponse.class);
        }

    @Override
    public Employeeresponse updateEmployee(Long id, EmployeeRequest request) {
        Employee employee = employeeRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Employee not found with id: " + id));

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setDepartment(request.getDepartment());
        employee.setPosition(request.getPoistion());
        employee.setSalary(request.getSalary());

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            employee.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        Employee update = employeeRepo.save(employee);
        return modelMapper.map(update, Employeeresponse.class);
    }




    @Override
    public Employeeresponse getEmployeeById(Long employeeId) {
        Employee employee = employeeRepo.findById(employeeId).orElseThrow(()->new ResourceNotFoundException("Employee not found with id: " + employeeId));
        return modelMapper.map(employee, Employeeresponse.class);
    }

    @Override
    public List<Employeeresponse> getAllEmployees()
    {
        List<Employee> employees = employeeRepo.findAll();

        return employees.stream().map(employee -> modelMapper.map(employee, Employeeresponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteEmployee(Long employeeId)
    {
        if(!employeeRepo.existsById(employeeId))
        {
            throw new ResourceNotFoundException("Employee not found with id: " + employeeId);
        }
        employeeRepo.deleteById(employeeId);

    }


//    @Override
//    public LoginResponse login(LoginRequest loginRequest) throws Exception {
//        Employee employees;
//        try {
//             employees = employeeRepo.findByEmail(loginRequest.getEmail()).get(0);
//        }
//        catch(Exception e){
//            throw new Exception("Employee not found with provided email");
//        }
//
//
//        if (!passwordEncoder.matches(loginRequest.getPassword(), employees.getPassword())) {
//            throw new InvalidCredentialsException("Invalid password");
//        }
//
//        LoginResponse response = new LoginResponse();
//        response.setEmployee(modelMapper.map(employees, Employeeresponse.class));
//
//        response.setLoginTime(LocalDateTime.now());
//        response.setMessage("Logged in successfully");
//        return response;
//    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) throws Exception {
        Employee employees;
        employees = employeeRepo.findByEmail(loginRequest.getEmail());
        if(employees==null)
        {
            throw new Exception("Employee not found with provided email");
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), employees.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        LoginResponse response = new LoginResponse();
        response.setEmployee(modelMapper.map(employees, Employeeresponse.class));
        response.setLoginTime(LocalDateTime.now());
        response.setMessage("Logged in successfully");
        response.setStatus("SUCCESS");

        // Generate JWT token
        String token = jwtUtil.generateToken(employees.getEmail());
        response.setToken(token);

        return response;
    }
}
