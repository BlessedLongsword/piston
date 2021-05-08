package com.example.piston.data;

public class NotificationReply extends Notification {

    private String user, content;

    public NotificationReply() {
    }

    public NotificationReply(String user, String content, boolean read,
                             String collection, String sectionID, String postID) {
        super(collection, sectionID, postID, read);
        this.user = user;
        this.content = content;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
