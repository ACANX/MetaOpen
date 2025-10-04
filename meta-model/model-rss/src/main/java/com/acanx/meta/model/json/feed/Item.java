package com.acanx.meta.model.json.feed;

import lombok.Data;

import java.util.List;

/**
 * Item
 *
 * @author ACANX
 * @since 20250926
 */
@Data
public class Item {

    private String id;
    private String title;
    private String url;
    private String contentHtml;
    private String contentText;


    private String externalUrl;

    private String summary;

    private String image;

    private String bannerImage;

    private String datePublished;

    private String dateModified;

    private List<Author> authors;

    private List<String>  tags;

    private String language;

    private List<Attachment> attachments;
}
