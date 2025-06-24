package com.acanx.meta.model.maven;

import org.apache.maven.model.RepositoryPolicy;

/**
 * PluginRepository
 *
 * @author ACANX
 * @since 20250623
 */
public class PluginRepository {

    private String id;

    private String name;

    private String url;

    private RepositoryPolicy releases;


    private RepositoryPolicy snapshots;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RepositoryPolicy getReleases() {
        return releases;
    }

    public void setReleases(RepositoryPolicy releases) {
        this.releases = releases;
    }

    public RepositoryPolicy getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(RepositoryPolicy snapshots) {
        this.snapshots = snapshots;
    }
}
