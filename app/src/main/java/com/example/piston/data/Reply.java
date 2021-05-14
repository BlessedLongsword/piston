package com.example.piston.data;

public class Reply {

    private String content;
    private String owner;
    private String id;

    public Reply() {}

    public Reply(String owner, String content, String id) {
        setContent(content);
        setOwner(owner);
        setId(id);
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
}
