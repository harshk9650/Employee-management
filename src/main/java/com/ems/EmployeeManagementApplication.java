package com.ems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * EmployeeManagementApplication - Main entry point of the Spring Boot application.
 *
 * @SpringBootApplication is a convenience annotation that combines:
 *   - @Configuration        : marks class as a source of bean definitions
 *   - @EnableAutoConfiguration : tells Spring Boot to auto-configure based on classpath
 *   - @ComponentScan        : scans the current package and sub-packages for Spring components
 */
@SpringBootApplication
public class EmployeeManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeManagementApplication.class, args);
        System.out.println(" Employee Management System started successfully!");
        System.out.println(" API available at: http://localhost:8080/employees");
    }
}
