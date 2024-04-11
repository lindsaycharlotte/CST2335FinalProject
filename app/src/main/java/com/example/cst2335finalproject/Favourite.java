package com.example.cst2335finalproject;

public class Favourite {
    protected String article;
    protected String category;
    protected String url;
    protected long id;
    protected Favourite favourite;

    /**
     *
     * @param a
     * @param c
     * @param u
     * @param i
     */
    public Favourite(String a, String c, String u, long i) {
        article = a;
        category = c;
        url = u;
        id = i;
    }

    /**
     *
     * @param a
     * @param c
     * @param u
     */
    public void update(String a, String c, String u) {
        article = a;
        category = c;
        url = u;
    }

    /**
     *
     * @return
     */
    public String getArticle() {
        return article;
    }

    /**
     *
     * @return
     */
    public String getCategory() {
        return category;
    }

    /**
     *
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     *
     * @return
     */
    public Favourite getFavourite() {
        return favourite;
    }
}
