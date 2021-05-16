package com.example.piston.data.notifications;

public class NotificationReply extends Notification {

    private String user, content, replyID;

    @SuppressWarnings("unused")
    public NotificationReply() {}

    public NotificationReply(String user, String content, String replyID, String userImageLink,
                             boolean read, String scope, String sectionID, String postID) {
        super(scope, sectionID, postID, userImageLink, read);
        this.user = user;
        this.content = content;
        this.replyID = replyID;
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

    public String getReplyID() {
        return replyID;
    }

    public void setReplyID(String replyID) {
        this.replyID = replyID;
    }
}
