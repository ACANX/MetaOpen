package com.acanx.meta.model.gemini;

import java.util.List;

/**
 * GeminiResponse
 *
 * @author ACANX
 * @since 20251008
 */
public class GeminiResponse {
    private List<Candidate> candidates;

    private UsageMetadata usageMetadata;

    private String modelVersion;

     private String responseId;

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public UsageMetadata getUsageMetadata() {
        return usageMetadata;
    }

    public void setUsageMetadata(UsageMetadata usageMetadata) {
        this.usageMetadata = usageMetadata;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }
}
