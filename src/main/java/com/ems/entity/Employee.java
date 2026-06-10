package com.ems.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Employee Entity - Maps to the `employee` table in MySQL.
 *
 * JPA Annotations used:
 *   @Entity         -> marks this class as a JPA-managed entity
 *   @Table          -> specifies the table name in the database
 *   @Id             -> marks the primary key field
 *   @GeneratedValue -> auto-increments the ID
 *   @Column         -> configures column constraints (nullable, unique, length)
 *
 * Lombok Annotations:
 *   @Data           -> generates getters, setters, toString, equals, hashCode
 *   @NoArgsConstructor -> generates a no-argument constructor
 *   @AllArgsConstructor -> generates a constructor with all fields
 *   @Builder        -> enables builder pattern for object creation
 *
 * Validation Annotations (Jakarta Bean Validation):
 *   @NotBlank       -> field must not be null or empty string
 *   @NotNull        -> field must not be null
 *   @Email          -> validates email format
 *   @Size           -> validates string length
 *   @Min / @DecimalMin -> validates numeric minimums
 */
@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    /**
     * Primary Key - auto-incremented by MySQL.
     * Strategy.IDENTITY uses MySQL's AUTO_INCREMENT.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Employee full name.
     * - Cannot be blank
     * - Length between 2 and 100 characters
     */
    @NotBlank(message = "Employee name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * Employee email address.
     * - Must be a valid email format
     * - Must be unique across all employees
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid format (e.g. user@example.com)")
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    /**
     * Department the employee belongs to.
     * e.g. Engineering, HR, Finance, Marketing
     */
    @NotBlank(message = "Department is required")
    @Size(max = 100, message = "Department name must not exceed 100 characters")
    @Column(name = "department", nullable = false, length = 100)
    private String department;

    /**
     * Employee's job title / designation.
     * e.g. Software Engineer, Team Lead, Manager
     */
    @NotBlank(message = "Designation is required")
    @Size(max = 100, message = "Designation must not exceed 100 characters")
    @Column(name = "designation", nullable = false, length = 100)
    private String designation;

    /**
     * Employee's monthly salary.
     * - Must be greater than 0
     * - Stored as DECIMAL(15,2) in MySQL for precision
     */
    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0.01", message = "Salary must be greater than 0")
    @Column(name = "salary", nullable = false, precision = 15, scale = 2)
    private BigDecimal salary;
}
