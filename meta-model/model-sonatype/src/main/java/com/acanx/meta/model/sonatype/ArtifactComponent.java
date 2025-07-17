package com.acanx.meta.model.sonatype;


import java.util.List;
import java.util.Objects;

/**
 * MavenVersion
 *
 */
public class ArtifactComponent {

   /**
    * 构件全ID (group:artifact:version)
    */
   private String id;

   private String namespace;

   private String name;


   private LatestVersionInfo latestVersionInfo;

   private Integer dependencyOfCount;

   private Integer dependentOnCount;

   private String type;

   private Integer childCount;

   private String description;

   /**
    *  标签
    */
   private List<String> categories;

   /**
    *  代码贡献者
    */
   private List<String> contributors;

   /**
    *  版本号
    */
   private String version;


   /**
    *  发布时间戳
    */
   private Long publishedEpochMillis;


   private List<String> licenses;


   public ArtifactComponent() {
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getNamespace() {
      return namespace;
   }

   public void setNamespace(String namespace) {
      this.namespace = namespace;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public LatestVersionInfo getLatestVersionInfo() {
      return latestVersionInfo;
   }

   public void setLatestVersionInfo(LatestVersionInfo latestVersionInfo) {
      this.latestVersionInfo = latestVersionInfo;
   }

   public Integer getDependencyOfCount() {
      return dependencyOfCount;
   }

   public void setDependencyOfCount(Integer dependencyOfCount) {
      this.dependencyOfCount = dependencyOfCount;
   }

   public Integer getDependentOnCount() {
      return dependentOnCount;
   }

   public void setDependentOnCount(Integer dependentOnCount) {
      this.dependentOnCount = dependentOnCount;
   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public Integer getChildCount() {
      return childCount;
   }

   public void setChildCount(Integer childCount) {
      this.childCount = childCount;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public List<String> getContributors() {
      return contributors;
   }

   public void setContributors(List<String> contributors) {
      this.contributors = contributors;
   }

   public String getVersion() {
      return version;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public Long getPublishedEpochMillis() {
      return publishedEpochMillis;
   }

   public void setPublishedEpochMillis(Long publishedEpochMillis) {
      this.publishedEpochMillis = publishedEpochMillis;
   }

   public List<String> getLicenses() {
      return licenses;
   }

   public void setLicenses(List<String> licenses) {
      this.licenses = licenses;
   }

   public List<String> getCategories() {
      return categories;
   }

   public void setCategories(List<String> categories) {
      this.categories = categories;
   }

   @Override
   public boolean equals(Object object) {
      if (this == object) return true;
      if (object == null || getClass() != object.getClass()) return false;

      ArtifactComponent that = (ArtifactComponent) object;
      return Objects.equals(getId(), that.getId()) && Objects.equals(getNamespace(), that.getNamespace()) && Objects.equals(getName(), that.getName()) && Objects.equals(getLatestVersionInfo(), that.getLatestVersionInfo()) && Objects.equals(getDependencyOfCount(), that.getDependencyOfCount()) && Objects.equals(getDependentOnCount(), that.getDependentOnCount()) && Objects.equals(getType(), that.getType()) && Objects.equals(getChildCount(), that.getChildCount()) && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getContributors(), that.getContributors()) && Objects.equals(getVersion(), that.getVersion()) && Objects.equals(getPublishedEpochMillis(), that.getPublishedEpochMillis()) && Objects.equals(getLicenses(), that.getLicenses());
   }

   @Override
   public int hashCode() {
      int result = Objects.hashCode(getId());
      result = 31 * result + Objects.hashCode(getNamespace());
      result = 31 * result + Objects.hashCode(getName());
      result = 31 * result + Objects.hashCode(getLatestVersionInfo());
      result = 31 * result + Objects.hashCode(getDependencyOfCount());
      result = 31 * result + Objects.hashCode(getDependentOnCount());
      result = 31 * result + Objects.hashCode(getType());
      result = 31 * result + Objects.hashCode(getChildCount());
      result = 31 * result + Objects.hashCode(getDescription());
      result = 31 * result + Objects.hashCode(getCategories());
      result = 31 * result + Objects.hashCode(getContributors());
      result = 31 * result + Objects.hashCode(getVersion());
      result = 31 * result + Objects.hashCode(getPublishedEpochMillis());
      result = 31 * result + Objects.hashCode(getLicenses());
      return result;
   }

   @Override
   public String toString() {
      return "ArtifactComponent{" +
              "id='" + id + '\'' +
              ", namespace='" + namespace + '\'' +
              ", name='" + name + '\'' +
              ", latestVersionInfo=" + latestVersionInfo +
              ", dependencyOfCount=" + dependencyOfCount +
              ", dependentOnCount=" + dependentOnCount +
              ", type='" + type + '\'' +
              ", childCount=" + childCount +
              ", description='" + description + '\'' +
              ", categories=" + categories +
              ", contributors=" + contributors +
              ", version='" + version + '\'' +
              ", publishedEpochMillis=" + publishedEpochMillis +
              ", licenses=" + licenses +
              '}';
   }
}
