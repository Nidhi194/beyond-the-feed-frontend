package com.beyondthefeed.model;

public class Article {
    private int id;
    private String title;
    private String slug;
    private String excerpt;
    private String body;
    private String category;
    private String categorySlug;
    private String author;
    private String readTime;
    private String image;
    private boolean featured;
    private String source;
    private String sourceUrl;
    private String publishedAt;

    public Article() { }

    public Article(int id, String title, String slug, String excerpt, String body, String category, String categorySlug, String author, String readTime, String image, boolean featured, String source, String sourceUrl, String publishedAt) {
        this.id = id; this.title = title; this.slug = slug; this.excerpt = excerpt; this.body = body; this.category = category; this.categorySlug = categorySlug; this.author = author; this.readTime = readTime; this.image = image; this.featured = featured; this.source = source; this.sourceUrl = sourceUrl; this.publishedAt = publishedAt;
    }
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getSlug() { return slug; }
    public String getExcerpt() { return excerpt; }
    public String getBody() { return body; }
    public String getCategory() { return category; }
    public String getCategorySlug() { return categorySlug; }
    public String getAuthor() { return author; }
    public String getReadTime() { return readTime; }
    public String getImage() { return image; }
    public boolean isFeatured() { return featured; }
    public String getSource() { return source; }
    public String getSourceUrl() { return sourceUrl; }
    public String getPublishedAt() { return publishedAt; }
}
