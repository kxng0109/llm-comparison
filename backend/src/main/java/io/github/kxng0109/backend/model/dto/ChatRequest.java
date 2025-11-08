package io.github.kxng0109.backend.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Represents a request for generating a response using one or more Language Learning Models (LLMs).
 *
 * The ChatRequest record defines a structure for clients to specify the input prompt
 * and the list of language models to be utilized for generating responses. It enables the
 * initiation of communication with a system that supports multiple models, allowing for
 * comparison, experimentation, or specific model targeting based on the application's needs.
 *
 * Fields:
 * - `prompt`: A non-empty string containing the text prompt that serves as the input for the LLM(s).
 *   This is the primary query or instruction that the language models will process.
 * - `llms`: A non-null list of strings specifying the identifiers of the language models that will
 *   be used to generate responses. Each identifier corresponds to a particular model instance.
 *
 * Key Features:
 * - Enforces constraints such as non-empty prompts and ensures the presence of at least one model identifier.
 * - Designed to facilitate interoperability with backend components that handle multi-model interactions.
 * - Immutable and compact, ensuring thread safety and reliability in concurrent environments.
 */
public record ChatRequest(
        @NotEmpty String prompt,
        @NotNull List<String> llms
) {
}
