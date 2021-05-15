package com.example.piston.data;

public class Reply {

    private String content, owner, id, imageLink;

    public Reply() {}

    public Reply(String owner, String content, String id, String imageLink) {
        setContent(content);
        setOwner(owner);
        setId(id);
        setImageLink(imageLink);
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

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getImageLink() {
        return imageLink;
    }
}
