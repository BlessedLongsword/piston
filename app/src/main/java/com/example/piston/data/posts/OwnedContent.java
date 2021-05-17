package com.example.piston.data.posts;

public abstract class OwnedContent {

    private String id;
    private String owner;
    private String ownerEmail;
    private String content;

    public OwnedContent() {}

    public OwnedContent(String id, String owner, String ownerEmail, String content) {
        setId(id);
        setOwner(owner);
        setOwnerEmail(ownerEmail);
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

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }
}
