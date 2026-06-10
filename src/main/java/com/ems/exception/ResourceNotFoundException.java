package com.ems.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ResourceNotFoundException - Thrown when a requested Employee is not found in the database.
 *
 * @ResponseStatus(HttpStatus.NOT_FOUND) instructs Spring MVC to automatically
 * return HTTP 404 if this exception is thrown and not caught by a handler.
 *
 * However, in our project the GlobalExceptionHandler intercepts this first
 * and returns a structured JSON error response.
 *
 * Extends RuntimeException so it does NOT need to be declared in method signatures
 * (unchecked exception) — cleaner service layer code.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private final String resourceName;  // e.g. "Employee"
    private final String fieldName;     // e.g. "id"
    private final Object fieldValue;    // e.g. 42

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        // Human-readable message: "Employee not found with id : 42"
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceName() { return resourceName; }
    public String getFieldName()    { return fieldName;    }
    public Object getFieldValue()   { return fieldValue;   }
}
