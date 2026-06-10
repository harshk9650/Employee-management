package com.ems.exception;

import com.ems.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler - Centralized exception handling for the entire application.
 *
 * @RestControllerAdvice:
 *   - Combines @ControllerAdvice + @ResponseBody
 *   - Intercepts exceptions thrown from ANY @RestController
 *   - Returns JSON error responses instead of HTML error pages
 *
 * Without this class, Spring Boot would return its default "Whitelabel Error Page"
 * (HTML) or a plain JSON error from BasicErrorController — neither of which
 * matches our ApiResponse<T> structure.
 *
 * Each @ExceptionHandler method handles a specific exception type and maps it
 * to the appropriate HTTP status code + JSON error body.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ------------------------------------------------------------------
    // 1. Employee not found → 404 Not Found
    // ------------------------------------------------------------------

    /**
     * Handles ResourceNotFoundException.
     * Triggered when GET/PUT/DELETE is called with a non-existent employee ID.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(
            ResourceNotFoundException ex) {

        ApiResponse<Void> response = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // ------------------------------------------------------------------
    // 2. Duplicate email → 409 Conflict
    // ------------------------------------------------------------------

    /**
     * Handles DuplicateEmailException.
     * Triggered when attempting to create/update an employee with an already-used email.
     */
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateEmailException(
            DuplicateEmailException ex) {

        ApiResponse<Void> response = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // ------------------------------------------------------------------
    // 3. Validation errors → 400 Bad Request
    // ------------------------------------------------------------------

    /**
     * Handles @Valid annotation failures (MethodArgumentNotValidException).
     * Triggered when the request body fails bean validation rules
     * (e.g. blank name, invalid email format, null salary).
     *
     * Collects ALL field errors and returns them as a map:
     * {
     *   "name": "Employee name is required",
     *   "email": "Email must be a valid format",
     *   "salary": "Salary must be greater than 0"
     * }
     *
     * The map is embedded in ApiResponse.data so the client gets structured errors.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();

        // Iterate over all field-level validation failures
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .success(false)
                .message("Validation failed. Please check the errors below.")
                .data(fieldErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // ------------------------------------------------------------------
    // 4. All other exceptions → 500 Internal Server Error
    // ------------------------------------------------------------------

    /**
     * Catch-all handler for unexpected exceptions.
     * Prevents stack traces from leaking to the client.
     * In production, log the exception here instead of printing it.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {

        // In production: log.error("Unexpected error", ex);
        System.err.println("Unexpected error: " + ex.getMessage());

        ApiResponse<Void> response = ApiResponse.error(
                "An unexpected error occurred. Please contact support.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
