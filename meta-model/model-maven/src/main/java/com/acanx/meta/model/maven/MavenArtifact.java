package com.acanx.meta.model.maven;

import java.util.List;

/**
 * Artifact
 *
 * @author ACANX
 * @since 20250622
 */

public class MavenArtifact {

    private String modelVersion;

    private String groupId;

    private String artifactId;

    private String version;

    private List<String> versions;

    private String latestVersion;

    private String lastUpdateDateTime;

    private String packaging;

    private String type;

    private String scope;

    private String name;

    private String description;

    private String url;

    private String scmUrl;

    private String organizationUrl;

    private String originDataProvider;

    public MavenArtifact() {
    }

    public MavenArtifact(String groupId, String artifactId) {
        this.groupId = groupId;
        this.artifactId = artifactId;
    }

    public MavenArtifact(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }


    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public List<String> getVersions() {
        return versions;
    }

    public void setVersions(List<String> versions) {
        this.versions = versions;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public String getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(String lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getOriginDataProvider() {
        return originDataProvider;
    }

    public void setOriginDataProvider(String originDataProvider) {
        this.originDataProvider = originDataProvider;
    }

    public String getScmUrl() {
        return scmUrl;
    }

    public void setScmUrl(String scmUrl) {
        this.scmUrl = scmUrl;
    }

    public String getOrganizationUrl() {
        return organizationUrl;
    }

    public void setOrganizationUrl(String organizationUrl) {
        this.organizationUrl = organizationUrl;
    }



    @Override
    public String toString() {
        return "MavenArtifact{" +
                "modelVersion='" + modelVersion + '\'' +
                ", groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                ", versions=" + versions +
                ", latestVersion='" + latestVersion + '\'' +
                ", lastUpdateDateTime='" + lastUpdateDateTime + '\'' +
                ", packaging='" + packaging + '\'' +
                ", type='" + type + '\'' +
                ", scope='" + scope + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", scmUrl='" + scmUrl + '\'' +
                ", organizationUrl='" + organizationUrl + '\'' +
                ", originDataProvider='" + originDataProvider + '\'' +
                '}';
    }




}
