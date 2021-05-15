package com.example.piston.data.posts;

public class Reply extends OwnedContent {

    private String ownerImageLink;

    public Reply() {}

    public Reply(String id, String owner, String content, String ownerImageLink) {
        super(id, owner, content);
        setOwnerImageLink(ownerImageLink);
    }

    public void setOwnerImageLink(String ownerImageLink) {
        this.ownerImageLink = ownerImageLink;
    }

    public String getOwnerImageLink() {
        return ownerImageLink;
    }
}
