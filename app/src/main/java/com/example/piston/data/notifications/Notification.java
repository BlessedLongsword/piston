package com.example.piston.data.notifications;

public abstract class Notification {

    private String scope;
    private String sectionID;
    private String postID;
    private String userImageLink;
    private boolean read;

    public Notification() {}

    public Notification(String scope, String sectionID, String postID, String userImageLink, boolean read) {
        setScope(scope);
        setSectionID(sectionID);
        setPostID(postID);
        setRead(read);
        setUserImageLink(userImageLink);
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

    public String getUserImageLink() {
        return userImageLink;
    }

    public void setUserImageLink(String userImageLink) {
        this.userImageLink = userImageLink;
    }
}
