package com.acanx.meta.component.maven.artifact;


import com.acanx.meta.model.maven.MavenArtifact;
import com.acanx.util.json.JSONUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArtifactServiceTest {

    public static final String GROUP_ID = "com.acanx.util";

    public static final String ARTIFACT_ID = "autil-core";

    public static final String VERSION = "0.2.0.2";

    @Test
    void getMavenArtifactFromMetaDataFile() {
       MavenArtifact mavenArtifact = ArtifactService.getMavenArtifactFromMetaDataFile(GROUP_ID, ARTIFACT_ID);
       System.out.println(mavenArtifact.toString());
       System.out.println(JSONUtil.toJSONStringPrettyFormat(mavenArtifact));

       assertNotNull(mavenArtifact);
    }

    @Test
    void getArtifactFromLatestVersionPomFile() {
        MavenArtifact mavenArtifact = ArtifactService.getArtifactFromLatestVersionPomFile(GROUP_ID, ARTIFACT_ID, VERSION);
        System.out.println(JSONUtil.toJSONStringPrettyFormat(mavenArtifact));
        assertNotNull(mavenArtifact);
    }
}