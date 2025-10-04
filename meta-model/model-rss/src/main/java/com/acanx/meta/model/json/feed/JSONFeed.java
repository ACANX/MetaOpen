package com.acanx.meta.model.json.feed;

import lombok.Data;

import java.util.List;

/**
 * JSONFeed
 *
 * @author ACANX
 * @since 20250926
 */
@Data
public class JSONFeed {

    private String title;

    private String version;

    private String userComment;

    private String homePageUrl;

    private String feedUrl;

    private String description;

    private String icon;

    private String favicon;

    private String language;

    private Boolean expired;

    private List<Author> authors;

    private List<Item> items;

}
