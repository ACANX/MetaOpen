package com.acanx.meta.model.sonatype;


import java.util.List;
import java.util.Objects;

/**
 * LatestVersionInfo
 *
 */
public class LatestVersionInfo {

    private List<String> licenses;

    private Long timestampUnixWithMS;


    private String version;

    public LatestVersionInfo() {
    }

    public List<String> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<String> licenses) {
        this.licenses = licenses;
    }

    public Long getTimestampUnixWithMS() {
        return timestampUnixWithMS;
    }

    public void setTimestampUnixWithMS(Long timestampUnixWithMS) {
        this.timestampUnixWithMS = timestampUnixWithMS;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        LatestVersionInfo that = (LatestVersionInfo) object;
        return Objects.equals(getLicenses(), that.getLicenses()) && Objects.equals(getTimestampUnixWithMS(), that.getTimestampUnixWithMS()) && Objects.equals(getVersion(), that.getVersion());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getLicenses());
        result = 31 * result + Objects.hashCode(getTimestampUnixWithMS());
        result = 31 * result + Objects.hashCode(getVersion());
        return result;
    }


    @Override
    public String toString() {
        return "LatestVersionInfo{" +
                "licenses=" + licenses +
                ", timestampUnixWithMS=" + timestampUnixWithMS +
                ", version='" + version + '\'' +
                '}';
    }
}
