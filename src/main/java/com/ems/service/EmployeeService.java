package com.ems.service;

import com.ems.dto.EmployeeDTO;

import java.util.List;

/**
 * EmployeeService - Service layer interface defining the business contract.
 *
 * Why define an interface?
 * -----------------------------------------------------------------------
 * 1. Abstraction / Loose coupling:
 *    The Controller depends on this interface, not the concrete implementation.
 *    This means we can swap implementations without touching the controller.
 *
 * 2. Testability:
 *    In unit tests, we can mock this interface instead of hitting the real DB.
 *
 * 3. Clean architecture principle:
 *    The interface lives in the `service` package; the implementation
 *    lives in `serviceimpl`. This mirrors real enterprise project layouts.
 *
 * 4. Spring proxying:
 *    Spring's @Transactional and AOP work best on interfaces.
 *
 * All methods operate on EmployeeDTO (not entity) to keep the service
 * layer decoupled from the persistence layer.
 */
public interface EmployeeService {

    /**
     * Create a new employee record.
     *
     * @param employeeDTO  the employee data from the request body
     * @return the created employee including the auto-generated ID
     * @throws com.ems.exception.DuplicateEmailException if email already exists
     */
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    /**
     * Retrieve all employees from the database.
     *
     * @return list of all employees (empty list if none exist)
     */
    List<EmployeeDTO> getAllEmployees();

    /**
     * Retrieve a single employee by their ID.
     *
     * @param id  the employee's database ID
     * @return the matching employee DTO
     * @throws com.ems.exception.ResourceNotFoundException if ID not found
     */
    EmployeeDTO getEmployeeById(Long id);

    /**
     * Update an existing employee's details.
     *
     * @param id           the ID of the employee to update
     * @param employeeDTO  the new data to apply
     * @return the updated employee DTO
     * @throws com.ems.exception.ResourceNotFoundException if ID not found
     * @throws com.ems.exception.DuplicateEmailException  if new email belongs to another employee
     */
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);

    /**
     * Delete an employee by their ID.
     *
     * @param id  the ID of the employee to delete
     * @throws com.ems.exception.ResourceNotFoundException if ID not found
     */
    void deleteEmployee(Long id);
}
