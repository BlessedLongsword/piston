package com.example.piston.data;

public class Post {

    private String title;
    private String content;
    private String owner;

    public Post() {}

    public Post(String title, String content, String owner) {
        setTitle(title);
        setContent(content);
        setOwner(owner);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
