package com.example.piston.data;

public class Notification {

    private String collection, sectionID, postID;
    private boolean read;

    public Notification() {}

    public Notification(String collection, String sectionID, String postID, boolean read) {
        this.collection = collection;
        this.sectionID = sectionID;
        this.postID = postID;
        this.read = read;
    }

    public boolean getIsRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getSectionID() {
        return sectionID;
    }

    public void setSectionID(String sectionID) {
        this.sectionID = sectionID;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }
}
