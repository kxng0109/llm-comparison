package io.github.kxng0109.backend.service;

import io.github.kxng0109.backend.error.ModelNotFoundException;
import io.github.kxng0109.backend.model.dto.ChatRequest;
import io.github.kxng0109.backend.model.dto.ModelResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.metadata.ChatGenerationMetadata;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.DefaultUsage;
import org.springframework.ai.chat.metadata.EmptyRateLimit;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AiServiceTest {

    @Mock
    private Map<String, ChatClient> chatClients;

    @Mock
    private ChatClient openAiChatClient;

    @Mock
    private ChatClient anthropicChatClient;

    @Mock
    private ChatClient ollamaChatClient;

    @InjectMocks
    private AiService aiService;


    @Test
    void testGetAvailableModels() {
        Map<String, ChatClient> testClients = new HashMap<>();
        testClients.put("openai", openAiChatClient);
        testClients.put("anthropic", anthropicChatClient);
        testClients.put("ollama", ollamaChatClient);

        when(chatClients.keySet()).thenReturn(testClients.keySet());

        Set<String> availableModels = aiService.getAvailableModels();

        assertNotNull(availableModels);
        assertEquals(3, availableModels.size());
        assertTrue(availableModels.contains("openai"));
        assertTrue(availableModels.contains("anthropic"));
        assertTrue(availableModels.contains("ollama"));
    }

    @Test
    void testCompareModels_Success() {
        String prompt = "What is AI?";
        List<String> llms = List.of("openai");
        ChatRequest chatRequest = new ChatRequest(prompt, llms);

        ChatClient.ChatClientRequestSpec requestSpec = mock(ChatClient.ChatClientRequestSpec.class);
        ChatClient.CallResponseSpec callResponseSpec = mock(ChatClient.CallResponseSpec.class);

        when(chatClients.containsKey("openai")).thenReturn(true);
        when(chatClients.get("openai")).thenReturn(openAiChatClient);
        when(openAiChatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.system(anyString())).thenReturn(requestSpec);
        when(requestSpec.user(eq(prompt))).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(callResponseSpec);

        ChatResponse mockResponse = createMockChatResponse(
                "AI is artificial intelligence",
                "gpt-4",
                100,
                200,
                300
        );
        when(callResponseSpec.chatResponse()).thenReturn(mockResponse);

        List<ModelResponse> responses = aiService.compareModels(chatRequest);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("openai", responses.getFirst().llm());
        assertTrue(responses.getFirst().response().contains("artificial intelligence"));

        verify(chatClients).containsKey("openai");
        verify(chatClients).get("openai");
    }

    @Test
    void testCompareModels_WithInvalidModel_ThrowsException() {
        String prompt = "What is AI?";
        List<String> llms = List.of("invalid-model");
        ChatRequest chatRequest = new ChatRequest(prompt, llms);

        when(chatClients.containsKey("invalid-model")).thenReturn(false);

        assertThrows(ModelNotFoundException.class, () -> {
                         aiService.compareModels(chatRequest);
                     }
        );
    }

    @Test
    void testSendMessage() {
        String llmName = "openai";
        String prompt = "Hello, AI!";

        ChatClient.ChatClientRequestSpec requestSpec = mock(ChatClient.ChatClientRequestSpec.class);
        ChatClient.CallResponseSpec callResponseSpec = mock(ChatClient.CallResponseSpec.class);

        when(openAiChatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.system(anyString())).thenReturn(requestSpec);
        when(requestSpec.user(eq(prompt))).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(callResponseSpec);

        ChatResponse expectedResponse = createMockChatResponse(
                "Hello! How can I help you?",
                "gpt-4",
                50,
                100,
                150
        );
        when(callResponseSpec.chatResponse()).thenReturn(expectedResponse);

        ChatResponse actualResponse = aiService.sendMessage(openAiChatClient, prompt);

        assertNotNull(actualResponse);
        assertEquals("Hello! How can I help you?",
                     actualResponse.getResult().getOutput().getText()
        );
        verify(requestSpec).system(anyString());
        verify(requestSpec).user(eq(prompt));
    }

    @Test
    void testCompareModels_HandlesException() {
        String prompt = "What is AI?";
        List<String> llms = List.of("openai");
        ChatRequest chatRequest = new ChatRequest(prompt, llms);

        ChatClient.ChatClientRequestSpec requestSpec = mock(ChatClient.ChatClientRequestSpec.class);
        ChatClient.CallResponseSpec callResponseSpec = mock(ChatClient.CallResponseSpec.class);

        when(chatClients.containsKey("openai")).thenReturn(true);
        when(chatClients.get("openai")).thenReturn(openAiChatClient);
        when(openAiChatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.system(anyString())).thenReturn(requestSpec);
        when(requestSpec.user(eq(prompt))).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(callResponseSpec);
        when(callResponseSpec.chatResponse()).thenThrow(new RuntimeException("API Error"));

        List<ModelResponse> responses = aiService.compareModels(chatRequest);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertTrue(responses.getFirst().response().startsWith("Error:"));
    }

    private ChatResponse createMockChatResponse(String content, String model,
                                                int promptTokens, int completionTokens,
                                                int totalTokens) {
        AssistantMessage assistantMessage = new AssistantMessage(content);

        ChatGenerationMetadata generationMetadata = ChatGenerationMetadata.builder()
                                                                          .finishReason("stop")
                                                                          .build();

        Generation generation = new Generation(assistantMessage, generationMetadata);

        DefaultUsage usage = new DefaultUsage(promptTokens, completionTokens, totalTokens);

        ChatResponseMetadata metadata = ChatResponseMetadata.builder()
                                                            .model(model)
                                                            .usage(usage)
                                                            .rateLimit(new EmptyRateLimit())
                                                            .build();

        return new ChatResponse(List.of(generation), metadata);
    }
}