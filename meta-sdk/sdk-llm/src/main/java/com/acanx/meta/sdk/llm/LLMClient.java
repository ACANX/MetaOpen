package com.acanx.meta.sdk.llm;

import com.acanx.meta.sdk.llm.model.ChatRequest;
import com.acanx.meta.sdk.llm.model.ChatResponse;
import com.acanx.meta.sdk.llm.model.LLMClientConfig;

/**
 * Unified client for chat-style large model APIs.
 */
public interface LLMClient {

    ChatResponse chat(ChatRequest request);

    static LLMClient create(LLMClientConfig config) {
        return new DefaultModelClient(config);
    }
}
