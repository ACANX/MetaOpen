package com.acanx.meta.model.rss;

import lombok.Data;

/**
 * Item
 *
 * @author ACANX
 * @since 20251004
 */
@Data
public class Item {

    private String title;

    private String link;

    private String description;

    private String pubDate;

    private String pubDateTime;

    private String guid;

    private String author;

    private String category;

    private String comments;

    private String enclosure;

    private String source;

    private String duration;

    private String keywords;

    private String urlImage;
}
