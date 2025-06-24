package com.acanx.meta.model.maven;

import java.util.List;

/**
 * Versioning
 *
 * @author ACANX
 * @since 20250623
 */
public class Versioning {

    private String latest;
    private String release;
    private String lastUpdated;

    private List<String> versions;

    public Versioning() {
    }

    public String getLatest() {
        return latest;
    }

    public void setLatest(String latest) {
        this.latest = latest;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public List<String> getVersions() {
        return versions;
    }

    public void setVersions(List<String> versions) {
        this.versions = versions;
    }
}
