package com.acanx.meta.model.maven;

import org.apache.maven.model.Build;
import org.apache.maven.model.CiManagement;
import org.apache.maven.model.Contributor;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Developer;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.IssueManagement;
import org.apache.maven.model.MailingList;
import org.apache.maven.model.Organization;
import org.apache.maven.model.Prerequisites;
import org.apache.maven.model.Profile;
import org.apache.maven.model.Reporting;
import org.apache.maven.model.Repository;
import org.apache.maven.model.Scm;

import java.util.List;

/**
 * Project
 *
 * @author ACANX
 * @since 20250623
 */
public class Project {


    private String modelVersion;

    private String groupId;

    private String artifactId;

    private String version;

    private String packaging;

    private String classifier;

    private String description;

    private String url;

    private String licenses;

    private String schemaLocation;

    private List<String> dependencies;

    private String name;

    private String parent;

    private Scm scm;

    private IssueManagement issueManagement;

    private Integer inceptionYear;

    private List<Developer> developers;


    private Organization organization;


    private List<String> properties;


    private DependencyManagement dependencyManagement;


    private List<Repository> repositories;

    private List<PluginRepository> pluginRepositories;

    private Build build;


    private List<Profile> profiles;

    private List<Contributor> contributors;

    private Prerequisites prerequisites;

    private CiManagement ciManagement;

    private DistributionManagement distributionManagement;


    private Reporting reporting;

    private List<MailingList> mailingLists;


    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getClassifier() {
        return classifier;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLicenses() {
        return licenses;
    }

    public void setLicenses(String licenses) {
        this.licenses = licenses;
    }

    public String getSchemaLocation() {
        return schemaLocation;
    }

    public void setSchemaLocation(String schemaLocation) {
        this.schemaLocation = schemaLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Scm getScm() {
        return scm;
    }

    public void setScm(Scm scm) {
        this.scm = scm;
    }

    public IssueManagement getIssueManagement() {
        return issueManagement;
    }

    public void setIssueManagement(IssueManagement issueManagement) {
        this.issueManagement = issueManagement;
    }

    public Integer getInceptionYear() {
        return inceptionYear;
    }

    public void setInceptionYear(Integer inceptionYear) {
        this.inceptionYear = inceptionYear;
    }

    public List<Developer> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<Developer> developers) {
        this.developers = developers;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public List<String> getProperties() {
        return properties;
    }

    public void setProperties(List<String> properties) {
        this.properties = properties;
    }

    public DependencyManagement getDependencyManagement() {
        return dependencyManagement;
    }

    public void setDependencyManagement(DependencyManagement dependencyManagement) {
        this.dependencyManagement = dependencyManagement;
    }

    public List<Repository> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<Repository> repositories) {
        this.repositories = repositories;
    }

    public List<PluginRepository> getPluginRepositories() {
        return pluginRepositories;
    }

    public void setPluginRepositories(List<PluginRepository> pluginRepositories) {
        this.pluginRepositories = pluginRepositories;
    }

    public Build getBuild() {
        return build;
    }

    public void setBuild(Build build) {
        this.build = build;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

    public List<Contributor> getContributors() {
        return contributors;
    }

    public void setContributors(List<Contributor> contributors) {
        this.contributors = contributors;
    }

    public Prerequisites getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(Prerequisites prerequisites) {
        this.prerequisites = prerequisites;
    }

    public DistributionManagement getDistributionManagement() {
        return distributionManagement;
    }

    public void setDistributionManagement(DistributionManagement distributionManagement) {
        this.distributionManagement = distributionManagement;
    }

    public CiManagement getCiManagement() {
        return ciManagement;
    }

    public void setCiManagement(CiManagement ciManagement) {
        this.ciManagement = ciManagement;
    }

    public Reporting getReporting() {
        return reporting;
    }

    public void setReporting(Reporting reporting) {
        this.reporting = reporting;
    }

    public List<MailingList> getMailingLists() {
        return mailingLists;
    }

    public void setMailingLists(List<MailingList> mailingLists) {
        this.mailingLists = mailingLists;
    }
}
