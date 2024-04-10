package com.example.cst2335finalproject;

public class Favourite {
    protected String article;
    protected String category;
    protected String url;
    protected long id;
    protected Favourite favourite;

    public Favourite(String a, String c, String u, long i) {
        article = a;
        category = c;
        url = u;
        id = i;
    }

    public void update(String a, String c, String u) {
        article = a;
        category = c;
        url = u;
    }

    public String getArticle() {
        return article;
    }

    public String getCategory() {
        return category;
    }

    public String getUrl() {
        return url;
    }

    public long getId() {
        return id;
    }

    public Favourite getFavourite() {
        return favourite;
    }
}
