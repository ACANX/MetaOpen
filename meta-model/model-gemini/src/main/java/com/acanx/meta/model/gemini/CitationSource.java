package com.acanx.meta.model.gemini;

/**
 * CitationSource
 *
 * @author ACANX
 * @since 20251009
 */
public class CitationSource {

    private Integer startIndex;
    private Integer endIndex;
    private String uri;
    private String license;

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
