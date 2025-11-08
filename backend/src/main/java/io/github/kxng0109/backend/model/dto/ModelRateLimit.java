package io.github.kxng0109.backend.model.dto;

import lombok.Builder;

/**
 * Represents the rate limiting information for a system or API usage.
 *
 * This record encapsulates the constraints and usage details regarding
 * requests and tokens within a given rate limiting framework. It provides
 * key details for monitoring and managing resource allocations to ensure
 * compliance with defined limits.
 *
 * Fields:
 * - `requestsLimit`: The maximum number of requests allowed within the current time window.
 * - `requestsRemaining`: The number of requests still available before reaching the limit.
 * - `tokensLimit`: The maximum number of tokens that can be used within the current time window.
 * - `tokensRemaining`: The number of tokens still available before reaching the token limit.
 * - `resetAfter`: The time, in seconds, remaining until the rate limits are reset.
 *
 * This record is immutable and uses the builder pattern for its creation,
 * ensuring concise and thread-safe initialization. It is particularly useful
 * for systems that require strict monitoring of resource consumption and
 * adherence to defined operational quotas.
 */
@Builder
public record ModelRateLimit(
        Long requestsLimit,
        Long requestsRemaining,
        Long tokensLimit,
        Long tokensRemaining,
        int resetAfter
) {
}
