package com.example.piston.data.notifications;

public abstract class Notification {

    private String scope;
    private String sectionID;
    private String postID;
    private String contextImageLink;
    private String notificationID;
    private boolean read;

    public Notification() {}

    public Notification(String scope, String sectionID, String postID, String contextImageLink, boolean read, String notificationID) {
        setScope(scope);
        setSectionID(sectionID);
        setPostID(postID);
        setRead(read);
        setContextImageLink(contextImageLink);
        setNotificationID(notificationID);
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

    public boolean getRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getContextImageLink() {
        return contextImageLink;
    }

    public void setContextImageLink(String contextImageLink) {
        this.contextImageLink = contextImageLink;
    }

    public String getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }
}
