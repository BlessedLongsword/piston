package com.example.piston.model;

public class Post {

    private String title;
    private String content;
    private String owner;
    private int id;

    public Post(String title, String content, String owner, int id) {
        setTitle(title);
        setContent(content);
        setOwner(owner);
        setId(id);
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


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
