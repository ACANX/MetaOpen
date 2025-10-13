package com.acanx.meta.model.gemini;

/**
 * Candidate
 *
 * @author ACANX
 * @since 20251008
 */
public class Candidate {
    private Content content;
    private String finishReason;
    private Integer index;

    private CitationMetadata citationMetadata;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }


    public CitationMetadata getCitationMetadata() {
        return citationMetadata;
    }

    public void setCitationMetadata(CitationMetadata citationMetadata) {
        this.citationMetadata = citationMetadata;
    }
}
