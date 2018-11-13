package com.dvipersquad.heybeach.data;

public class Beach {

    private String id;
    private String name;
    private String url;
    private String title;
    private int width;
    private int height;

    public Beach(String id, String name, String url, int width, int height) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public Beach(String id, String name, String url, String title, int width, int height) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.title = title;
        this.width = width;
        this.height = height;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
