package io.github.kxng0109.backend.controller;

import io.github.kxng0109.backend.model.dto.ChatRequest;
import io.github.kxng0109.backend.model.dto.ModelResponse;
import io.github.kxng0109.backend.service.AiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Controller for handling API requests related to language models (LLMs).
 * Provides endpoints to retrieve available language models, compare responses
 * from multiple models, and perform health checks for the API service.
 *
 * Endpoints:
 * - GET /api/llm/available: Fetch available language model identifiers.
 * - POST /api/llm/compare: Compare responses across multiple language models.
 * - GET /api/llm/health: Perform an application health check.
 */
@RestController
@RequestMapping("/api/llm")
@RequiredArgsConstructor
@Slf4j
public class AiController {

    private final AiService aiService;

    /**
     * Retrieves a list of available language model identifiers supported by the system.
     * <p>
     * This endpoint allows clients to fetch the unique identifiers of all
     * language models currently available for use. Each identifier represents a
     * language model that can handle requests within the system.
     *
     * @return a ResponseEntity containing a set of strings, where each string
     * is the unique identifier of an available language model.
     */
    @GetMapping("/available")
    public ResponseEntity<Set<String>> availableModels() {
        return ResponseEntity.ok(aiService.getAvailableModels());
    }

    /**
     * Compares responses from multiple language models using a specified prompt and returns the results.
     * <p>
     * This method processes the provided prompt by sending it to the listed language models.
     * The results are returned in a structured format including the responses and relevant metadata
     * for each language model. The metadata provides insights into each model's performance and output.
     *
     * @param chatRequest the request object containing the following:
     *                    - The `prompt` to send to the language models.
     *                    - A list of `llms` (language model identifiers) for which the prompt will be evaluated.
     * @return a ResponseEntity containing a map where:
     * - The key is "responses".
     * - The value is a list of {@link ModelResponse} instances for each language model,
     * including responses and associated metadata.
     */
    @PostMapping("/compare")
    public ResponseEntity<Map<String, List<ModelResponse>>> getAnswer(@Valid @RequestBody ChatRequest chatRequest) {
        return ResponseEntity.ok(Map.of("responses", aiService.compareModels(chatRequest)));
    }

    /**
     * Performs a health check for the API service.
     *
     * This endpoint verifies the operational status of the application
     * and ensures that the service is available to handle requests.
     * Typically used for monitoring and diagnostic purposes.
     *
     * @return a ResponseEntity with no body and an HTTP 200 OK status
     *         if the application is healthy and operational.
     */
    @GetMapping("/health")
    public ResponseEntity<Void> health() {
        return ResponseEntity.ok().build();
    }
}
