package com.example.piston.data.posts;

public abstract class OwnedContent {

    private String id, owner, content;

    public OwnedContent() {}

    public OwnedContent(String id, String owner, String content) {
        setId(id);
        setOwner(owner);
        setContent(content);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
