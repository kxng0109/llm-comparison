package io.github.kxng0109.backend.service;

import io.github.kxng0109.backend.error.ModelNotFoundException;
import io.github.kxng0109.backend.model.dto.ChatRequest;
import io.github.kxng0109.backend.model.dto.ModelMetadata;
import io.github.kxng0109.backend.model.dto.ModelRateLimit;
import io.github.kxng0109.backend.model.dto.ModelResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.RateLimit;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The AiService class serves as the core service for managing and interacting with various language models.
 * It supports functionalities such as retrieving available language model identifiers, comparing responses
 * from multiple models, and processing interactions with a specified language model. This service is
 * designed for efficient language model communication and metadata generation.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AiService {
    private final Map<String, ChatClient> chatClients;

    String systemMessage = "You are chatting with a serious personal, make sure your responses are accurate, up-to-date, and are straight to the point unless the user asks you not to. False or wrong responses or poorly researched responses are not allowed here!";

    /**
     * Retrieves the set of identifiers for all available language models.
     * <p>
     * This method provides an overview of the different language models
     * currently supported and available for interaction in the system.
     * Each identifier represents a unique language model that can be
     * used to process user requests.
     *
     * @return a set of strings representing the identifiers of the available language models.
     */
    public Set<String> getAvailableModels() {
        return chatClients.keySet();
    }

    /**
     * Compares multiple language models (LLMs) by sending a request prompt to each model
     * and collecting their responses along with metadata such as processing time.
     *
     * @param chatRequest An instance of ChatRequest containing the prompt to be sent to
     *                    the language models and the list of model identifiers (llms) to be compared.
     * @return A list of ModelResponse objects containing the responses from each model and
     *         associated metadata such as model processing time. If an error occurs during
     *         the request to a specific model, the response for that model will include the error message.
     */
    public List<ModelResponse> compareModels(ChatRequest chatRequest) {
        List<String> llms = chatRequest.llms();
        String prompt = chatRequest.prompt();

        validateModels(llms);

        return llms.parallelStream().map(llm -> {
            try {
                ChatClient chatClient = chatClients.get(llm);
                long startTime = System.currentTimeMillis();

                ChatResponse chatResponse = sendMessage(chatClient, prompt);

                ModelMetadata modelMetadata = getModelMetadata(
                        chatResponse,
                        System.currentTimeMillis() - startTime
                );
                return ModelResponse.builder()
                                    .llm(llm)
                                    .response(chatResponse.getResult().getOutput().getText())
                                    .metadata(modelMetadata)
                                    .build();
            } catch (Exception e) {
                log.error("Error occurred: {}", e.getMessage(), e);
                return ModelResponse.builder()
                                    .llm(llm)
                                    .response("Error: " + e.getMessage())
                                    .build();
            }
        }).toList();
    }

    /**
     * Sends a prompt message to the specified language model via the provided ChatClient and returns the response.
     * <p>
     * This method logs the prompt being sent and facilitates interaction with the language model by preparing
     * and transmitting the request. The response is subsequently returned as a ChatResponse object.
     *
     * @param chatClient the ChatClient used to communicate with the language model
     * @param prompt     the user prompt or message to be sent to the language model
     * @return a ChatResponse object containing the result of the conversation, including the generated response
     * and associated metadata from the language model
     */
    public ChatResponse sendMessage(ChatClient chatClient, String prompt) {
        return chatClient.prompt()
                         .system(systemMessage)
                         .user(prompt)
                         .call()
                         .chatResponse();
    }

    /**
     * Extracts and constructs metadata regarding a model's response, including rate limits, token usage,
     * and response details, to encapsulate comprehensive processing statistics.
     * <p>
     * This method takes in the response from a language model, along with the time taken to generate
     * the response, processes the accompanying metadata, and returns a structured `ModelMetadata` object
     * containing various attributes like token counts, rate limits, and timestamp.
     *
     * @param chatResponse the response object received from the language model, containing output text
     *                     and associated metadata including token usage and rate limit details.
     * @param responseTime the time taken (in milliseconds) for the language model to process and return
     *                     the response.
     * @return a `ModelMetadata` object encapsulating metadata for the given response, such as token statistics,
     * rate limits, model identifier, finish reason, and the timestamp of processing.
     */
    private ModelMetadata getModelMetadata(ChatResponse chatResponse, Long responseTime) {
        ChatResponseMetadata responseMetadata = chatResponse.getMetadata();
        ModelRateLimit rateLimit = getModelRateLimit(responseMetadata.getRateLimit());
        Usage modelUsage = responseMetadata.getUsage();

        return ModelMetadata.builder()
                            .finishReason(chatResponse.getResult().getMetadata().getFinishReason())
                            .generationTokens(modelUsage.getCompletionTokens())
                            .responseTime(responseTime)
                            .promptTokens(modelUsage.getPromptTokens())
                            .totalTokens(modelUsage.getTotalTokens())
                            .timestamp(Instant.now().toString())
                            .model(responseMetadata.getModel())
                            .rateLimit(rateLimit)
                            .build();
    }

    /**
     * Converts a RateLimit object into a ModelRateLimit object by mapping its attributes.
     *
     * @param rateLimit the RateLimit object containing rate limit information, such as
     *                  request limits and token limits.
     * @return a ModelRateLimit object that encapsulates the corresponding rate limit
     * information, including the request and token limits and their remaining counts.
     */
    private ModelRateLimit getModelRateLimit(RateLimit rateLimit) {
        return ModelRateLimit.builder()
                             .requestsLimit(rateLimit.getRequestsLimit())
                             .requestsRemaining(rateLimit.getRequestsRemaining())
                             .tokensLimit(rateLimit.getTokensLimit())
                             .tokensRemaining(rateLimit.getTokensRemaining())
//                .resetAfter()
                             .build();
    }

    /**
     * Validates whether the provided list of model names exists in the available chat clients.
     * If any model in the list is not found, an exception is thrown indicating the unavailable models.
     *
     * @param llms a list of model names to be validated against the available chat clients
     * @throws ModelNotFoundException if one or more models in the provided list are not available
     */
    private void validateModels(List<String> llms) {
        List<String> invalidModels = llms.stream()
                                         .filter(llm -> !chatClients.containsKey(llm))
                                         .toList();

        if (!invalidModels.isEmpty()) {
            throw new ModelNotFoundException(
                    "The following models are not available: " + String.join(", ", invalidModels)
            );
        }
    }
}
