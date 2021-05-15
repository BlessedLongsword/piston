package com.example.piston.data.notifications;

public abstract class Notification {

    private String scope, sectionID, postID;
    private boolean read;

    public Notification() {}

    public Notification(String scope, String sectionID, String postID, boolean read) {
        setScope(scope);
        setSectionID(sectionID);
        setPostID(postID);
        setRead(read);
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
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

    public boolean getIsRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
