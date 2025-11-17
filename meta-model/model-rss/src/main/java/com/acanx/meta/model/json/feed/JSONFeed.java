package com.acanx.meta.model.json.feed;

import java.util.List;

/**
 * JSONFeed
 *
 * @author ACANX
 * @since 20250926
 */
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public String getHomePageUrl() {
        return homePageUrl;
    }

    public void setHomePageUrl(String homePageUrl) {
        this.homePageUrl = homePageUrl;
    }

    public String getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "JSONFeed{" +
                "title='" + title + '\'' +
                ", version='" + version + '\'' +
                ", userComment='" + userComment + '\'' +
                ", homePageUrl='" + homePageUrl + '\'' +
                ", feedUrl='" + feedUrl + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                ", favicon='" + favicon + '\'' +
                ", language='" + language + '\'' +
                ", expired=" + expired +
                ", authors=" + authors +
                ", items=" + items +
                '}';
    }
}
