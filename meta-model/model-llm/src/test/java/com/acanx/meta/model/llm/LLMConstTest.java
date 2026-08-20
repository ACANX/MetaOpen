package com.acanx.meta.model.llm;

import com.acanx.meta.model.llm.provider.AnthropicModel;
import com.acanx.meta.model.llm.provider.DeepSeekModel;
import com.acanx.meta.model.llm.provider.GeminiModel;
import com.acanx.meta.model.llm.provider.LLMBaseUrls;
import com.acanx.meta.model.llm.provider.OpenAIModel;
import com.acanx.meta.model.llm.provider.XiaomiModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * LLM 常量类测试
 *
 * 覆盖 LLMConst 与各 provider 常量类，确保 model-llm 模块产生 JaCoCo 报告。
 *
 * @author BeeAgent
 * @since 2026-08-20
 */
class LLMConstTest {

    @Test
    void shouldContainLLMConstValues() {
        assertEquals("x-api-key", LLMConst.HEADER_X_API_KEY);
        assertEquals("/chat/completions", LLMConst.PATH_CHAT_COMPLETIONS);
        assertEquals("v1beta", LLMConst.DEFAULT_GEMINI_API_VERSION);
        assertEquals("2023-06-01", LLMConst.DEFAULT_ANTHROPIC_VERSION);
    }

    @Test
    void shouldContainProviderBaseUrls() {
        assertEquals("https://api.openai.com/v1", LLMBaseUrls.OPENAI);
        assertEquals("https://api.deepseek.com", LLMBaseUrls.DEEPSEEK_OPENAI);
        assertEquals("https://api.anthropic.com/v1", LLMBaseUrls.ANTHROPIC);
        assertEquals("https://openrouter.ai/api/v1", LLMBaseUrls.OPENROUTER);
    }

    @Test
    void shouldContainProviderModels() {
        assertNotNull(OpenAIModel.GPT_5_5);
        assertNotNull(OpenAIModel.GPT_4O);
        assertNotNull(DeepSeekModel.V4_PRO);
        assertNotNull(DeepSeekModel.V4_FLASH);
        assertNotNull(AnthropicModel.CLAUDE_SONNET_4_6);
        assertNotNull(GeminiModel.GEMINI_3_1_PRO_PREVIEW);
        assertNotNull(XiaomiModel.MIMO_V2_OMNI);
    }
}
