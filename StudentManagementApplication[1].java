package com.example.studentmanagement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudentManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudentManagementApplication.class, args);
        System.out.println("========================================");
        System.out.println("Student Management System Started!");
        System.out.println("Access the application at: http://localhost:8080/");
        System.out.println("H2 Console at: http://localhost:8080/h2-console");
        System.out.println("========================================");
    }
}