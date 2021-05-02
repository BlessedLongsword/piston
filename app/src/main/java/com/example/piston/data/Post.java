package com.example.piston.data;

public class Post {

    private String title;
    private String content;
    private String owner;

    private String id;
    private String documentID;
    private String imageId;
    private String imageLink;

    public Post() {}

    public Post(String title, String content, String owner, String id, String documentID, String imageId, String imageLink) {
        setTitle(title);
        setContent(content);
        setOwner(owner);
        setId(id);
        setDocumentID(documentID);
        setImageId(imageId);
        setImageLink(imageLink);
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

    //Per quan borrem la imatge
    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }
}
