package com.ems.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * DuplicateEmailException - Thrown when an employee with the given email already exists.
 *
 * This is a business-rule exception:
 *   Email addresses must be unique per employee — enforced both at the
 *   DB level (UNIQUE constraint) and the service layer (proactive check).
 *
 * HTTP 409 Conflict is the semantically correct status code for this scenario:
 *   the request is valid but conflicts with the current state of the resource.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super(String.format("An employee with email '%s' already exists", email));
    }
}
