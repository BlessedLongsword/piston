package com.example.piston.data;

import com.google.firebase.firestore.Exclude;

public class Post {

    private String title;
    private String content;
    private String owner;

    private String id;
    private String image_id;

    private byte[] image;

    public Post() {}

    public Post(String title, String content, String owner, String id, String image_id) {
        setTitle(title);
        setContent(content);
        setOwner(owner);
        setId(id);
        setImage_id(image_id);
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    @Exclude
    public byte[] getImage() {
        return image;
    }

    @Exclude
    public void setImage(byte[] image) {
        this.image = image;
    }
}
