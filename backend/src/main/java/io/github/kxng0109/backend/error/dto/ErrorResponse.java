package io.github.kxng0109.backend.error.dto;

import lombok.Builder;

import java.util.Map;

/**
 * ErrorResponse is a record that represents the standard structure of an error response
 * in a system. It is commonly used to encapsulate and communicate details about errors
 * that occur during the execution of requests, providing meaningful information to the client.
 *
 * This record follows an immutable design and is built with key details to describe the
 * error, including the timestamp of the error occurrence, HTTP status code, error type,
 * a descriptive error message, and additional details if applicable.
 *
 * Fields:
 * - timestamp: The date and time when the error occurred, represented as a string.
 * - status: The HTTP status code associated with the error (e.g., 404, 500).
 * - error: A brief string describing the error type (e.g., "Not Found", "Internal Server Error").
 * - message: A more detailed message explaining the error to aid in understanding or debugging.
 * - details: A map containing additional key-value pairs to provide further context or metadata
 *   about the error, such as validation errors or request-specific information.
 */
@Builder
public record ErrorResponse(
        String timestamp,
        int status,
        String error,
        String message,
        Map<String, String> details
) {
}