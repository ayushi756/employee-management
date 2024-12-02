    package com.example.employee_management.entity;

    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    @Entity
    @Table(name = "employees")
    @Getter
    @Setter
    @NoArgsConstructor
    public class Employee
    {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(nullable = false)
        private String firstName;
        @Column(nullable = false)
        private String lastName;
        @Column(nullable = false, unique = true)
        private String email;
        @Column(nullable = false)
        private String password;
        @Column(nullable = false)
        private String department;
        private String position;
        private double salary;


    }