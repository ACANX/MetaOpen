package com.acanx.meta.model.gemini;

import java.util.List;

/**
 * CitationMetadata
 *
 * @author ACANX
 * @since 20251009
 */
public class CitationMetadata {

    private List<CitationSource> citationSources;

    public List<CitationSource> getCitationSources() {
        return citationSources;
    }

    public void setCitationSources(List<CitationSource> citationSources) {
        this.citationSources = citationSources;
    }
}
