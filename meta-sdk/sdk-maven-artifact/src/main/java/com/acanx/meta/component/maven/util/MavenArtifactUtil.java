package com.acanx.meta.component.maven.util;


import com.acanx.meta.component.maven.c.MVNC;

/**
 * MavenArtifactUtil
 *
 * @author ACANX
 * @since 20250622
 */
public class MavenArtifactUtil {

    /**
     *
     * @param groupId
     * @param artifactId
     * @return
     */
    public static String getArtifactMetadataXmlFileUrl(String groupId, String artifactId) {
        String groupPath = groupId.replace(".", "/");
        String reqUrl = MVNC.URL_MAVEN_CENTRAL_REPO + groupPath + "/" + artifactId.replace(".", "/") + "/maven-metadata.xml";
        return reqUrl;
    }

    /**
     *
     * @param groupId
     * @param artifactId
     * @param latestVersion
     * @return
     */
    public static String getArtifactLatestVersionPomFileUrl(String groupId, String artifactId, String latestVersion) {
        String pomFileUrl = String.format("%s%s/%s/%s/%s-%s.pom", MVNC.URL_MAVEN_CENTRAL_REPO, groupId.replace(".", "/"), artifactId, latestVersion, artifactId, latestVersion);
        return pomFileUrl;
    }

    /**
     *
     * @param groupId
     * @param artifactId
     * @return
     */
    public static String getMvnRepositoryUrl(String groupId, String artifactId) {
        return String.format("%s%s/%s", MVNC.URL_MVN_REPOSITORY_PREFIX, groupId, artifactId);
    }









}
