package com.acanx.meta.component.maven.artifact;


import com.acanx.meta.model.maven.MavenArtifact;
import com.acanx.util.JSONUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArtifactServiceTest {

    public static final String groupId = "com.acanx.util";

    public static final String artifactId = "autil-core";

    public static final String version = "0.2.0.2";

    public static final ArtifactService service =  new ArtifactService();

    @Test
    void getMavenArtifactFromMetaDataFile() {
       MavenArtifact mavenArtifact = service.getMavenArtifactFromMetaDataFile(groupId, artifactId);
       System.out.println(mavenArtifact.toString());
       System.out.println(JSONUtil.toJSONStringPrettyFormat(mavenArtifact));

       assertNotNull(mavenArtifact);
    }

    @Test
    void getArtifactFromLatestVersionPomFile() {
        MavenArtifact mavenArtifact = service.getArtifactFromLatestVersionPomFile(groupId, artifactId, version);
        System.out.println(JSONUtil.toJSONStringPrettyFormat(mavenArtifact));
        assertNotNull(mavenArtifact);
    }
}