package com.example.piston.model;

public class Post {

    private String title;
    private String content;
    private String picturePath;
    private int id;

    public Post(String title, String content, String picture, int id) {
        setTitle(title);
        setContent(content);
        setPicturePath(picture);
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

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picture) {
        this.picturePath = picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
