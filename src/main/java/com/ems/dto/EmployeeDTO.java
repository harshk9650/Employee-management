package com.ems.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * EmployeeDTO - Data Transfer Object (DTO) for Employee.
 *
 * Why use a DTO instead of directly exposing the Entity?
 * ---------------------------------------------------------
 * 1. Separation of concerns: entity is for DB, DTO is for API layer
 * 2. Security: avoids exposing internal fields (e.g. passwords, audit fields)
 * 3. Flexibility: API shape can evolve independently from DB schema
 * 4. Validation: validation lives in DTO, not the entity
 *
 * This DTO is used for both:
 *   - Request body (POST, PUT) — client sends this JSON
 *   - Response body (GET)      — server returns this JSON
 *   The `id` field is null on create, populated on read/update.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {

    /** Null on create (POST), populated on read/update responses */
    private Long id;

    @NotBlank(message = "Employee name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid format (e.g. user@example.com)")
    private String email;

    @NotBlank(message = "Department is required")
    @Size(max = 100, message = "Department name must not exceed 100 characters")
    private String department;

    @NotBlank(message = "Designation is required")
    @Size(max = 100, message = "Designation must not exceed 100 characters")
    private String designation;

    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0.01", message = "Salary must be greater than 0")
    private BigDecimal salary;
}
