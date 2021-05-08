package com.example.piston.data;

public class NotificationPost extends Notification {

    private String title, sectionName, imageLink;

    public NotificationPost() {
    }

    public NotificationPost(String title, String sectionName, String imageLink, boolean read,
                            String collection, String sectionID, String postID) {
        super(collection, sectionID, postID, read);
        this.title = title;
        this.sectionName = sectionName;
        this.imageLink = imageLink;
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
