package com.beyondthefeed.model;

public class Category {
    private int id;
    private String name;
    private String slug;
    private String description;

    public Category() { }

    public Category(int id, String name, String slug, String description) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSlug() { return slug; }
    public String getDescription() { return description; }
}
