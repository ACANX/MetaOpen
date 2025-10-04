package com.acanx.meta.model.json.feed;

import lombok.Data;

/**
 * Attachment
 *
 * @author ACANX
 * @since 20250926
 */
@Data
public class Attachment {

   private String url;

   private String mimeType;


   private String title;


   private String sizeInBytes;

   private String durationInSeconds;
}
