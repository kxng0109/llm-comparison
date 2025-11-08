package io.github.kxng0109.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kxng0109.backend.error.ModelNotFoundException;
import io.github.kxng0109.backend.model.dto.ChatRequest;
import io.github.kxng0109.backend.model.dto.ModelMetadata;
import io.github.kxng0109.backend.model.dto.ModelRateLimit;
import io.github.kxng0109.backend.model.dto.ModelResponse;
import io.github.kxng0109.backend.service.AiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AiController.class)
class AiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AiService aiService;

    @Test
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/api/llm/health"))
               .andExpect(status().isOk());
    }

    @Test
    void testAvailableModels() throws Exception {
        Set<String> models = Set.of("openai", "anthropic", "ollama");
        when(aiService.getAvailableModels()).thenReturn(models);

        mockMvc.perform(get("/api/llm/available"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$", hasSize(3)))
               .andExpect(jsonPath("$", hasItem("openai")))
               .andExpect(jsonPath("$", hasItem("anthropic")))
               .andExpect(jsonPath("$", hasItem("ollama")));
    }

    @Test
    void testCompareModels_Success() throws Exception {
        ChatRequest request = new ChatRequest("What is AI?", List.of("openai", "anthropic"));

        ModelRateLimit rateLimit = ModelRateLimit.builder()
                                                 .requestsLimit(10000L)
                                                 .requestsRemaining(9999L)
                                                 .tokensLimit(2000000L)
                                                 .tokensRemaining(1999000L)
                                                 .resetAfter(3600)
                                                 .build();

        ModelMetadata openAiMetadata = ModelMetadata.builder()
                                                    .promptTokens(25)
                                                    .generationTokens(150)
                                                    .totalTokens(175)
                                                    .responseTime(2340L)
                                                    .model("gpt-4-turbo")
                                                    .finishReason("stop")
                                                    .timestamp(Instant.now().toString())
                                                    .rateLimit(rateLimit)
                                                    .build();

        ModelMetadata anthropicMetadata = ModelMetadata.builder()
                                                       .promptTokens(30)
                                                       .generationTokens(160)
                                                       .totalTokens(190)
                                                       .responseTime(2500L)
                                                       .model("claude-3-opus")
                                                       .finishReason("stop")
                                                       .timestamp(Instant.now().toString())
                                                       .rateLimit(rateLimit)
                                                       .build();

        ModelResponse openAiResponse = ModelResponse.builder()
                                                    .llm("openai")
                                                    .response("AI is artificial intelligence from OpenAI")
                                                    .metadata(openAiMetadata)
                                                    .build();

        ModelResponse anthropicResponse = ModelResponse.builder()
                                                       .llm("anthropic")
                                                       .response("AI is artificial intelligence from Anthropic")
                                                       .metadata(anthropicMetadata)
                                                       .build();

        when(aiService.compareModels(any(ChatRequest.class)))
                .thenReturn(List.of(openAiResponse, anthropicResponse));

        mockMvc.perform(post("/api/llm/compare")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.responses", hasSize(2)))
               .andExpect(jsonPath("$.responses[0].llm", is("openai")))
               .andExpect(jsonPath("$.responses[0].response",
                                   containsString("artificial intelligence")
               ))
               .andExpect(jsonPath("$.responses[0].metadata.model", is("gpt-4-turbo")))
               .andExpect(jsonPath("$.responses[0].metadata.promptTokens", is(25)))
               .andExpect(jsonPath("$.responses[0].metadata.generationTokens", is(150)))
               .andExpect(jsonPath("$.responses[0].metadata.totalTokens", is(175)))
               .andExpect(jsonPath("$.responses[1].llm", is("anthropic")))
               .andExpect(jsonPath("$.responses[1].metadata.model", is("claude-3-opus")));
    }

    @Test
    void testCompareModels_EmptyPrompt() throws Exception {
        ChatRequest request = new ChatRequest("", List.of("openai"));

        mockMvc.perform(post("/api/llm/compare")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.error", is("Validation Failed")));
    }

    @Test
    void testCompareModels_NullLlmsList() throws Exception {
        ChatRequest request = new ChatRequest("What is AI?", null);

        mockMvc.perform(post("/api/llm/compare")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.error", is("Validation Failed")));
    }

    @Test
    void testCompareModels_ModelNotFound() throws Exception {
        ChatRequest request = new ChatRequest("What is AI?", List.of("invalid-model"));

        when(aiService.compareModels(any(ChatRequest.class)))
                .thenThrow(new ModelNotFoundException("Model 'invalid-model' not found"));

        mockMvc.perform(post("/api/llm/compare")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.error", is("Model Not Found")))
               .andExpect(jsonPath("$.message", containsString("invalid-model")));
    }

    @Test
    void testCompareModels_InvalidJson() throws Exception {
        mockMvc.perform(post("/api/llm/compare")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{invalid json}"))
               .andExpect(status().isBadRequest());
    }

    @Test
    void testCompareModels_MissingContentType() throws Exception {
        ChatRequest request = new ChatRequest("What is AI?", List.of("openai"));

        mockMvc.perform(post("/api/llm/compare")
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isUnsupportedMediaType());
    }
}