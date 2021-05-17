package com.example.piston.data.notifications;

public class NotificationPost extends Notification {

    private String title, sectionName, imageLink;

    @SuppressWarnings("unused")
    public NotificationPost() {}

    public NotificationPost(String title, String sectionName, String imageLink, String userImageLink, boolean read,
                            String collection, String sectionID, String postID) {
        super(collection, sectionID, postID, userImageLink, read);
        setTitle(title);
        setSectionName(sectionName);
        setImageLink(imageLink);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
