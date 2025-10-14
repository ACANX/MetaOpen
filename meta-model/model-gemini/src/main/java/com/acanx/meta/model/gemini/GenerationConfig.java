package com.acanx.meta.model.gemini;

/**
 * GenerationConfig
 *
 * @author ACANX
 * @since 20251008
 */
public class GenerationConfig {
    private Double temperature;
    private Integer maxOutputTokens;

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getMaxOutputTokens() {
        return maxOutputTokens;
    }

    public void setMaxOutputTokens(Integer maxOutputTokens) {
        this.maxOutputTokens = maxOutputTokens;
    }
}
