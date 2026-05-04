package com.acanx.meta.sdk.llm;

import com.acanx.meta.base.http.c.BaseHttpConst;
import com.acanx.meta.model.llm.LLMConst;
import com.acanx.meta.sdk.llm.enums.ModelProvider;
import com.acanx.meta.sdk.llm.model.ChatMessage;
import com.acanx.meta.sdk.llm.model.ChatRequest;
import com.acanx.meta.sdk.llm.model.ChatResponse;
import com.acanx.meta.sdk.llm.model.LLMClientConfig;
import com.acanx.meta.model.llm.provider.AnthropicModel;
import com.acanx.meta.model.llm.provider.DeepSeekModel;
import com.acanx.meta.model.llm.provider.GeminiModel;
import com.acanx.meta.model.llm.provider.XiaomiModel;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LLMClientTest {

    @Test
    void chatWithOpenAiCompatibleProvider() throws IOException {
        AtomicReference<String> path = new AtomicReference<>();
        AtomicReference<String> auth = new AtomicReference<>();
        try (TestServer server = TestServer.start(exchange -> {
            path.set(exchange.getRequestURI().getPath());
            auth.set(exchange.getRequestHeaders().getFirst(BaseHttpConst.HEADER_AUTHORIZATION));
            respond(exchange, """
                    {"id":"chatcmpl-test","model":"test-model","choices":[{"index":0,"message":{"role":"assistant","content":"ok"},"finish_reason":"stop"}],"usage":{"prompt_tokens":1,"completion_tokens":2,"total_tokens":3}}
                    """);
        })) {
            LLMClient client = LLMClient.create(LLMClientConfig.builder(ModelProvider.OPENAI_COMPATIBLE)
                    .baseUrl(server.baseUrl())
                    .apiKey("test-key")
                    .build());
            ChatResponse response = client.chat(ChatRequest.builder("test-model")
                    .message(ChatMessage.user("hello"))
                    .build());
            assertEquals(LLMConst.PATH_CHAT_COMPLETIONS, path.get());
            assertEquals(BaseHttpConst.AUTH_SCHEME_BEARER + " test-key", auth.get());
            assertEquals("ok", response.firstText());
            assertEquals(3, response.getUsage().getTotalTokens());
        }
    }

    @Test
    void chatWithDeepSeekV4Models() throws IOException {
        for (String model : List.of(DeepSeekModel.V4_PRO, DeepSeekModel.V4_FLASH)) {
            AtomicReference<String> path = new AtomicReference<>();
            AtomicReference<String> auth = new AtomicReference<>();
            AtomicReference<String> requestBody = new AtomicReference<>();
            try (TestServer server = TestServer.start(exchange -> {
                path.set(exchange.getRequestURI().getPath());
                auth.set(exchange.getRequestHeaders().getFirst(BaseHttpConst.HEADER_AUTHORIZATION));
                requestBody.set(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
                respond(exchange, """
                        {"id":"deepseek-test","model":"%s","choices":[{"index":0,"message":{"role":"assistant","content":"deepseek ok"},"finish_reason":"stop"}],"usage":{"prompt_tokens":3,"completion_tokens":4,"total_tokens":7}}
                        """.formatted(model));
            })) {
                LLMClient client = LLMClient.create(LLMClientConfig.builder(ModelProvider.DEEPSEEK)
                        .baseUrl(server.baseUrl())
                        .apiKey("deepseek-key")
                        .build());
                ChatResponse response = client.chat(ChatRequest.builder(model)
                        .message(ChatMessage.user("hello"))
                        .build());
                assertEquals(LLMConst.PATH_CHAT_COMPLETIONS, path.get());
                assertEquals(BaseHttpConst.AUTH_SCHEME_BEARER + " deepseek-key", auth.get());
                assertTrue(requestBody.get().contains("\"model\":\"" + model + "\""));
                assertEquals(model, response.getModel());
                assertEquals("deepseek ok", response.firstText());
                assertEquals(7, response.getUsage().getTotalTokens());
            }
        }
    }

    @Test
    void chatWithXiaomiMimoV25ProOpenAiCompatibleModelIds() throws IOException {
        for (String model : List.of(XiaomiModel.MIMO_V2_5_PRO, XiaomiModel.MIMO_V2_5_PRO_OPENROUTER)) {
            AtomicReference<String> path = new AtomicReference<>();
            AtomicReference<String> requestBody = new AtomicReference<>();
            try (TestServer server = TestServer.start(exchange -> {
                path.set(exchange.getRequestURI().getPath());
                requestBody.set(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
                respond(exchange, """
                        {"id":"mimo-test","model":"%s","choices":[{"index":0,"message":{"role":"assistant","content":"mimo ok"},"finish_reason":"stop"}],"usage":{"prompt_tokens":5,"completion_tokens":6,"total_tokens":11}}
                        """.formatted(model));
            })) {
                LLMClient client = LLMClient.create(LLMClientConfig.builder(ModelProvider.OPENAI_COMPATIBLE)
                        .baseUrl(server.baseUrl())
                        .apiKey("mimo-key")
                        .build());
                ChatResponse response = client.chat(ChatRequest.builder(model)
                        .message(ChatMessage.user("hello"))
                        .build());
                assertEquals(LLMConst.PATH_CHAT_COMPLETIONS, path.get());
                assertTrue(requestBody.get().contains("\"model\":\"" + model + "\""));
                assertEquals(model, response.getModel());
                assertEquals("mimo ok", response.firstText());
                assertEquals(11, response.getUsage().getTotalTokens());
            }
        }
    }

    @Test
    void chatWithGeminiProvider() throws IOException {
        AtomicReference<String> path = new AtomicReference<>();
        AtomicReference<String> apiKey = new AtomicReference<>();
        try (TestServer server = TestServer.start(exchange -> {
            path.set(exchange.getRequestURI().getPath());
            apiKey.set(exchange.getRequestHeaders().getFirst(LLMConst.HEADER_X_GOOG_API_KEY));
            respond(exchange, """
                    {"modelVersion":"gemini-test","candidates":[{"content":{"parts":[{"text":"gemini ok"}]},"finishReason":"STOP"}],"usageMetadata":{"promptTokenCount":4,"candidatesTokenCount":5,"totalTokenCount":9}}
                    """);
        })) {
            LLMClient client = LLMClient.create(LLMClientConfig.builder(ModelProvider.GEMINI)
                    .baseUrl(server.baseUrl())
                    .apiKey("gemini-key")
                    .build());
            ChatResponse response = client.chat(ChatRequest.builder(GeminiModel.GEMINI_TEST)
                    .message(ChatMessage.user("hello"))
                    .build());
            assertEquals(LLMConst.PATH_V1_BETA + LLMConst.PATH_MODELS + "/gemini-test" + LLMConst.PATH_GENERATE_CONTENT, path.get());
            assertEquals("gemini-key", apiKey.get());
            assertEquals("gemini ok", response.firstText());
            assertEquals(9, response.getUsage().getTotalTokens());
        }
    }

    @Test
    void chatWithGeminiV1Provider() throws IOException {
        AtomicReference<String> path = new AtomicReference<>();
        try (TestServer server = TestServer.start(exchange -> {
            path.set(exchange.getRequestURI().getPath());
            respond(exchange, """
                    {"modelVersion":"gemini-v1-test","candidates":[{"content":{"parts":[{"text":"gemini v1 ok"}]},"finishReason":"STOP"}]}
                    """);
        })) {
            LLMClient client = LLMClient.create(LLMClientConfig.builder(ModelProvider.GEMINI)
                    .baseUrl(server.baseUrl())
                    .geminiApiVersion("v1")
                    .apiKey("gemini-key")
                    .build());
            ChatResponse response = client.chat(ChatRequest.builder(GeminiModel.GEMINI_V1_TEST)
                    .message(ChatMessage.user("hello"))
                    .build());
            assertEquals(LLMConst.PATH_V1 + LLMConst.PATH_MODELS + "/gemini-v1-test" + LLMConst.PATH_GENERATE_CONTENT, path.get());
            assertEquals("gemini v1 ok", response.firstText());
        }
    }

    @Test
    void chatWithGeminiBaseUrlThatAlreadyContainsVersion() throws IOException {
        AtomicReference<String> path = new AtomicReference<>();
        try (TestServer server = TestServer.start(exchange -> {
            path.set(exchange.getRequestURI().getPath());
            respond(exchange, """
                    {"modelVersion":"gemini-v1-test","candidates":[{"content":{"parts":[{"text":"gemini versioned base ok"}]},"finishReason":"STOP"}]}
                    """);
        })) {
            LLMClient client = LLMClient.create(LLMClientConfig.builder(ModelProvider.GEMINI)
                    .baseUrl(server.baseUrl() + LLMConst.PATH_V1)
                    .apiKey("gemini-key")
                    .build());
            ChatResponse response = client.chat(ChatRequest.builder(GeminiModel.GEMINI_V1_TEST)
                    .message(ChatMessage.user("hello"))
                    .build());
            assertEquals(LLMConst.PATH_V1 + LLMConst.PATH_MODELS + "/gemini-v1-test" + LLMConst.PATH_GENERATE_CONTENT, path.get());
            assertEquals("gemini versioned base ok", response.firstText());
        }
    }

    @Test
    void chatWithAnthropicProvider() throws IOException {
        AtomicReference<String> path = new AtomicReference<>();
        AtomicReference<String> version = new AtomicReference<>();
        try (TestServer server = TestServer.start(exchange -> {
            path.set(exchange.getRequestURI().getPath());
            version.set(exchange.getRequestHeaders().getFirst(LLMConst.HEADER_ANTHROPIC_VERSION));
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            assertTrue(requestBody.contains("\"system\":\"be brief\""));
            respond(exchange, """
                    {"id":"msg-test","model":"claude-test","content":[{"type":"text","text":"anthropic ok"}],"stop_reason":"end_turn","usage":{"input_tokens":6,"output_tokens":7}}
                    """);
        })) {
            LLMClient client = LLMClient.create(LLMClientConfig.builder(ModelProvider.ANTHROPIC)
                    .baseUrl(server.baseUrl())
                    .apiKey("anthropic-key")
                    .build());
            ChatResponse response = client.chat(ChatRequest.builder(AnthropicModel.CLAUDE_TEST)
                    .message(ChatMessage.system("be brief"))
                    .message(ChatMessage.user("hello"))
                    .build());
            assertEquals(LLMConst.PATH_MESSAGES, path.get());
            assertEquals("2023-06-01", version.get());
            assertEquals("anthropic ok", response.firstText());
            assertEquals(13, response.getUsage().getTotalTokens());
        }
    }

    @Test
    void chatWithAnthropicCompatibleProvider() throws IOException {
        AtomicReference<String> path = new AtomicReference<>();
        AtomicReference<String> apiKey = new AtomicReference<>();
        try (TestServer server = TestServer.start(exchange -> {
            path.set(exchange.getRequestURI().getPath());
            apiKey.set(exchange.getRequestHeaders().getFirst(LLMConst.HEADER_X_API_KEY));
            respond(exchange, """
                    {"id":"msg-compatible","model":"claude-compatible","content":[{"type":"text","text":"compatible ok"}],"stop_reason":"end_turn","usage":{"input_tokens":1,"output_tokens":2}}
                    """);
        })) {
            LLMClient client = LLMClient.create(LLMClientConfig.builder(ModelProvider.ANTHROPIC_COMPATIBLE)
                    .baseUrl(server.baseUrl())
                    .apiKey("compatible-key")
                    .build());
            ChatResponse response = client.chat(ChatRequest.builder(AnthropicModel.CLAUDE_COMPATIBLE)
                    .message(ChatMessage.user("hello"))
                    .build());
            assertEquals(LLMConst.PATH_MESSAGES, path.get());
            assertEquals("compatible-key", apiKey.get());
            assertEquals("compatible ok", response.firstText());
            assertEquals(3, response.getUsage().getTotalTokens());
        }
    }

    private static void respond(HttpExchange exchange, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set(BaseHttpConst.HEADER_CONTENT_TYPE, BaseHttpConst.MEDIA_TYPE_APPLICATION_JSON);
        exchange.sendResponseHeaders(BaseHttpConst.STATUS_OK, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    private static class TestServer implements AutoCloseable {
        private final HttpServer server;

        private TestServer(HttpServer server) {
            this.server = server;
        }

        static TestServer start(ExchangeHandler handler) throws IOException {
            HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
            server.createContext("/", handler::handle);
            server.start();
            return new TestServer(server);
        }

        String baseUrl() {
            return BaseHttpConst.SCHEME_HTTP + "://127.0.0.1:" + server.getAddress().getPort();
        }

        @Override
        public void close() {
            server.stop(0);
        }
    }

    private interface ExchangeHandler {
        void handle(HttpExchange exchange) throws IOException;
    }
}
