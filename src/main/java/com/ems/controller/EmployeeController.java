package com.ems.controller;

import com.ems.dto.ApiResponse;
import com.ems.dto.EmployeeDTO;
import com.ems.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * EmployeeController - REST API layer for Employee Management.
 *
 * Key annotations:
 *   @RestController:
 *     Combines @Controller + @ResponseBody.
 *     Every method return value is automatically serialized to JSON
 *     by Jackson (no need for @ResponseBody on each method).
 *
 *   @RequestMapping("/employees"):
 *     All endpoints in this controller are prefixed with /employees.
 *     e.g. POST /employees, GET /employees/{id}
 *
 *   @RequiredArgsConstructor (Lombok):
 *     Constructor injection of EmployeeService.
 *     This is preferred over @Autowired field injection because:
 *     - Makes dependencies explicit and immutable (final)
 *     - Easier to test (pass mock in constructor)
 *     - Avoids Spring dependency on tests
 *
 *   @CrossOrigin:
 *     Allows requests from any origin (useful during development with
 *     Postman or a React/Angular frontend on a different port).
 *     Restrict this to specific origins in production.
 *
 * Controller responsibilities:
 *   - Map HTTP methods + paths to handler methods
 *   - Parse and validate request bodies (@Valid @RequestBody)
 *   - Extract path variables (@PathVariable)
 *   - Delegate all business logic to the service layer
 *   - Wrap results in ApiResponse and return correct HTTP status codes
 *   - Never contain business logic or DB calls directly
 *
 * HTTP Status Codes used:
 *   201 Created    -> successful POST (resource created)
 *   200 OK         -> successful GET, PUT
 *   204 No Content -> successful DELETE (no body returned)
 *   400 Bad Request -> validation failures (handled by GlobalExceptionHandler)
 *   404 Not Found  -> resource doesn't exist (handled by GlobalExceptionHandler)
 *   409 Conflict   -> duplicate email (handled by GlobalExceptionHandler)
 */
@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class EmployeeController {

    private final EmployeeService employeeService;

    // ======================================================================
    // POST /employees  → Create Employee
    // ======================================================================

    /**
     * Creates a new employee.
     *
     * @RequestBody EmployeeDTO:
     *   Jackson deserializes the incoming JSON into an EmployeeDTO object.
     *
     * @Valid:
     *   Triggers Bean Validation on the EmployeeDTO fields.
     *   If validation fails, Spring throws MethodArgumentNotValidException
     *   which our GlobalExceptionHandler catches and returns 400 + field errors.
     *
     * ResponseEntity.status(HttpStatus.CREATED).body(...):
     *   Explicitly sets HTTP 201 Created — the semantically correct status
     *   for a successful resource creation (not 200 OK).
     */
    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeDTO>> createEmployee(
            @Valid @RequestBody EmployeeDTO employeeDTO) {

        log.info("POST /employees - Creating employee: {}", employeeDTO.getName());

        EmployeeDTO created = employeeService.createEmployee(employeeDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Employee created successfully", created));
    }

    // ======================================================================
    // GET /employees  → Get All Employees
    // ======================================================================

    /**
     * Returns all employees in the database.
     *
     * Returns HTTP 200 OK with a list (empty list if no employees exist).
     * We never return 404 for an empty list — empty list is a valid state.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getAllEmployees() {
        log.info("GET /employees - Fetching all employees");

        List<EmployeeDTO> employees = employeeService.getAllEmployees();

        String message = employees.isEmpty()
                ? "No employees found"
                : "Fetched " + employees.size() + " employee(s) successfully";

        return ResponseEntity.ok(ApiResponse.success(message, employees));
    }

    // ======================================================================
    // GET /employees/{id}  → Get Employee by ID
    // ======================================================================

    /**
     * Returns a single employee by their ID.
     *
     * @PathVariable Long id:
     *   Spring extracts the {id} segment from the URL and converts it to Long.
     *   e.g. GET /employees/5 → id = 5
     *
     * If the ID doesn't exist, the service throws ResourceNotFoundException,
     * caught by GlobalExceptionHandler → 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> getEmployeeById(
            @PathVariable Long id) {

        log.info("GET /employees/{} - Fetching employee by ID", id);

        EmployeeDTO employee = employeeService.getEmployeeById(id);

        return ResponseEntity.ok(
                ApiResponse.success("Employee fetched successfully", employee));
    }

    // ======================================================================
    // PUT /employees/{id}  → Update Employee
    // ======================================================================

    /**
     * Updates an existing employee's details.
     *
     * PUT is idempotent — calling it multiple times with the same data
     * produces the same result (unlike POST which creates a new resource each time).
     *
     * @Valid @RequestBody: validates the update payload too.
     * All fields are required in our design (full update, not partial).
     * For partial updates (PATCH), you'd use @PatchMapping with Optional fields.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO employeeDTO) {

        log.info("PUT /employees/{} - Updating employee", id);

        EmployeeDTO updated = employeeService.updateEmployee(id, employeeDTO);

        return ResponseEntity.ok(
                ApiResponse.success("Employee updated successfully", updated));
    }

    // ======================================================================
    // DELETE /employees/{id}  → Delete Employee
    // ======================================================================

    /**
     * Deletes an employee by ID.
     *
     * HTTP 200 OK with a success message (no data payload, hence Void).
     * Some APIs return 204 No Content (empty body) for deletes.
     * We return 200 + JSON message for consistency with our ApiResponse structure.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(
            @PathVariable Long id) {

        log.info("DELETE /employees/{} - Deleting employee", id);

        employeeService.deleteEmployee(id);

        return ResponseEntity.ok(
                ApiResponse.success("Employee with ID " + id + " deleted successfully"));
    }
}
