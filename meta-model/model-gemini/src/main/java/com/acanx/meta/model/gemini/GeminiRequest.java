package com.acanx.meta.model.gemini;


import java.util.List;

/**
 * GeminiRequest  Gemini 请求和响应DTO
 *
 * @author ACANX
 * @since 20251008
 */
public class GeminiRequest {
    private List<Content> contents;
    private GenerationConfig generationConfig;

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public GenerationConfig getGenerationConfig() {
        return generationConfig;
    }

    public void setGenerationConfig(GenerationConfig generationConfig) {
        this.generationConfig = generationConfig;
    }
}
