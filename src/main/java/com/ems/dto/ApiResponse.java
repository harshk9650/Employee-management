package com.ems.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ApiResponse - Generic wrapper for all API responses.
 *
 * Every endpoint returns this structure so the client always knows what to expect:
 * {
 *   "success": true,
 *   "message": "Employee created successfully",
 *   "data": { ...employee data... },
 *   "timestamp": "2024-06-10T10:30:00"
 * }
 *
 * @JsonInclude(NON_NULL) ensures that null fields (like `data` on delete)
 * are omitted from the JSON output.
 *
 * Generic type <T> allows this to wrap any payload type:
 *   ApiResponse<EmployeeDTO>      -> single employee
 *   ApiResponse<List<EmployeeDTO>> -> list of employees
 *   ApiResponse<Void>             -> no data (delete response)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    /** Whether the operation succeeded */
    private boolean success;

    /** Human-readable message describing the result */
    private String message;

    /** The actual payload — null for delete responses */
    private T data;

    /** When the response was generated (ISO 8601) */
    private LocalDateTime timestamp;

    // -------------------------------------------------------
    // Static factory helpers — keeps controllers clean
    // -------------------------------------------------------

    /** Build a success response with data payload */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /** Build a success response without data (e.g. delete) */
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /** Build an error response */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
