package com.acanx.meta.component.maven.artifact;


import com.acanx.constant.HTTPC;
import com.acanx.meta.component.maven.util.MavenArtifactUtil;
import com.acanx.meta.model.maven.MavenArtifact;
import com.acanx.meta.model.maven.Metadata;
import com.acanx.meta.model.maven.Project;
import com.acanx.util.StringUtil;
import com.acanx.util.http.BaseHTTP;
import com.acanx.util.http.HRequest;
import com.acanx.util.http.HResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.IOException;



/**
 * ArtifactService
 *
 * @author ACANX
 * @since 20250622
 */
public class ArtifactService {


    /**
     *
     * @param groupId   分组ID
     * @param artifactId  分组ID
     * @return   MavenArtifact对象
     */
    public static MavenArtifact getMavenArtifactFromMetaDataFile(String groupId, String artifactId) {
        String reqUrl = MavenArtifactUtil.getArtifactMetadataXmlFileUrl(groupId, artifactId);
        System.out.println(reqUrl);
        // getArtifactLatestVersionPomFileUrl
        MavenArtifact ma = new MavenArtifact(groupId, artifactId);
        HRequest request = HRequest.builder().method(HTTPC.GET).url(reqUrl).build();
        HResponse response = BaseHTTP.getHttpResponse(request);
        if (response.getStatusCode() == 200 && StringUtil.isNotBlank(response.getBody())) {
            String xmlResponse = response.getBody();
            Metadata metadata = null;
            try {
                metadata = parseMavenMetadataXmlContent(xmlResponse);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            ma.setLatestVersion(metadata.getVersioning().getLatest());
            ma.setLastUpdateDateTime(metadata.getVersioning().getLastUpdated());
            ma.setVersions(metadata.getVersioning().getVersions());
            ma.setPackaging(metadata.getVersion());
            // ma.setIgnoreFlag(Boolean.FALSE);
            ma.setOriginDataProvider("metadata.xml");
        } else {
            // ma.setIgnoreFlag(Boolean.TRUE);
            ma.setType("UNKNOWN");
            ma.setPackaging("UNKNOWN");
        }
        return ma;
    }

    /**
     *
     * @param groupId   分组ID
     * @param artifactId c
     * @param version   版本
     * @return   MavenArtifact对象
     */
    public static MavenArtifact getArtifactFromLatestVersionPomFile(String groupId, String artifactId, String version) {
        String reqUrl = MavenArtifactUtil.getArtifactLatestVersionPomFileUrl(groupId, artifactId, version);
        System.out.println(reqUrl);

        MavenArtifact ma = new MavenArtifact(groupId, artifactId);
        ma.setLatestVersion(version);
        ma.setVersion(version);
        HRequest request = HRequest.builder().method(HTTPC.GET).url(reqUrl).build();
        HResponse response = BaseHTTP.getHttpResponse(request);
        if (response.getStatusCode() == 200 && StringUtil.isNotBlank(response.getBody())) {
            String xmlResponse = response.getBody();
            Project project = null;
            String packagingType = "jar";
            try {
                project = parseMavenProjectPomXmlContent(xmlResponse);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (XmlPullParserException e) {
                throw new RuntimeException(e);
            }
            // System.out.println("Packaging type: " + project.getPackaging());
            if (null != project.getPackaging() && !project.getPackaging().isEmpty()) {
                packagingType = project.getPackaging();
            }
            getUrl(project, ma);
            getDescription(project, ma);
            getName(project, ma);
            getScmUrl(project, ma);
            getOrganizationUrl(project, ma);
            ma.setModelVersion(project.getModelVersion());
            ma.setPackaging(project.getVersion());
            ma.setType(packagingType);
            ma.setPackaging(packagingType);
            ma.setOriginDataProvider("pom.xml");
        } else {
            ma.setType("UNKNOWN");
            ma.setPackaging("UNKNOWN");
        }
        return ma;
    }


    private static Metadata parseMavenMetadataXmlContent(String xmlContent) throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
        return (Metadata)xmlMapper.readValue(xmlContent, Metadata.class);
    }


    private static Project parseMavenProjectPomXmlContent(String pomContent) throws IOException, XmlPullParserException {
        XmlMapper xmlMapper = new XmlMapper();
        // 忽略未知属性
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return xmlMapper.readValue(pomContent, Project.class);
    }

    private static void getUrl(Project project, MavenArtifact am) {
        if (null != project.getUrl()) {
            am.setUrl(project.getUrl());
        }
    }

    private static void getDescription(Project project, MavenArtifact am) {
        if (null != project.getDescription()) {
            am.setDescription(project.getDescription());
        }
    }

    private static void getScmUrl(Project project, MavenArtifact am) {
        if (null != project.getScm()) {
            if (null != project.getScm().getUrl()) {
                am.setScmUrl(project.getScm().getUrl());
            }
        }
    }

    private static void getName(Project project, MavenArtifact am) {
        if (null != project.getName()) {
            am.setName(project.getName());
        }
    }

    private static void getOrganizationUrl(Project project, MavenArtifact am) {
        if (null != project.getOrganization()) {
            if (null != project.getOrganization().getUrl()) {
                am.setOrganizationUrl(project.getOrganization().getUrl());
            }
        }
    }




}
