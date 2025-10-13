package com.acanx.meta.model.gemini;

/**
 * PromptTokensDetail
 *
 * @author ACANX
 * @since 20251009
 */

public class PromptTokensDetail {

    private String modality;

    private Integer tokenCount;


    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public Integer getTokenCount() {
        return tokenCount;
    }

    public void setTokenCount(Integer tokenCount) {
        this.tokenCount = tokenCount;
    }
}
