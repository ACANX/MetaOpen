package com.acanx.meta.model.json.feed;

/**
 * Attachment
 *
 * @author ACANX
 * @since 20250926
 */

public class Attachment {

   private String url;

   private String mimeType;


   private String title;


   private String sizeInBytes;

   private String durationInSeconds;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSizeInBytes() {
        return sizeInBytes;
    }

    public void setSizeInBytes(String sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    public String getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(String durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }
}
