package com.acanx.meta.model.wechat.work;


import java.util.List;

/**
 * News
 *
 */
public class News {

    private List<Article> articles;


    public News(List<Article> articles) {
        this.articles = articles;
    }


    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
        return "News{" +
                "articles=" + articles +
                '}';
    }
}
