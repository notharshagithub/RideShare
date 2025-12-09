package com.harsha.assignment.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionTest {

    @Test
    void testBadRequestException() {
        BadRequestException exception = new BadRequestException("Bad request message");

        assertEquals("Bad request message", exception.getMessage());
        assertNotNull(exception);
    }

    @Test
    void testNotFoundException() {
        NotFoundException exception = new NotFoundException("Resource not found");

        assertEquals("Resource not found", exception.getMessage());
        assertNotNull(exception);
    }

    @Test
    void testErrorResponse() {
        ErrorResponse errorResponse = new ErrorResponse(
            "2025-01-15T10:30:00",
            400,
            "Bad Request",
            "Invalid input",
            "/api/v1/rides"
        );

        assertEquals("2025-01-15T10:30:00", errorResponse.getTimestamp());
        assertEquals(400, errorResponse.getStatus());
        assertEquals("Bad Request", errorResponse.getError());
        assertEquals("Invalid input", errorResponse.getMessage());
        assertEquals("/api/v1/rides", errorResponse.getPath());
    }

    @Test
    void testBadRequestExceptionThrows() {
        assertThrows(BadRequestException.class, () -> {
            throw new BadRequestException("Test exception");
        });
    }

    @Test
    void testNotFoundExceptionThrows() {
        assertThrows(NotFoundException.class, () -> {
            throw new NotFoundException("Test exception");
        });
    }

    @Test
    void testErrorResponseSetters() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp("2025-01-15T10:30:00");
        errorResponse.setStatus(404);
        errorResponse.setError("Not Found");
        errorResponse.setMessage("Resource not found");
        errorResponse.setPath("/api/v1/rides/123");

        assertEquals("2025-01-15T10:30:00", errorResponse.getTimestamp());
        assertEquals(404, errorResponse.getStatus());
        assertEquals("Not Found", errorResponse.getError());
        assertEquals("Resource not found", errorResponse.getMessage());
        assertEquals("/api/v1/rides/123", errorResponse.getPath());
    }
}
