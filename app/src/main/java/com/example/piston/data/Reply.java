package com.example.piston.data;

public class Reply {

    private String content, owner, id;
    public Reply() {}

    public Reply(String owner, String content, String id) {
        setContent(content);
        setOwner(owner);
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
