package com.example.piston.data.posts;

import com.google.firebase.firestore.Exclude;

public class Post extends OwnedContent {

    private String title, sectionID, imageLink, profileImageLink;
    private int numLikes;
    private boolean pinned;

    public Post() {}

    public Post(String id, String owner, String ownerEmail, String content, String title, String sectionID,
                String imageLink, String profileImageLink) {
        super(id, owner, ownerEmail, content);
        setTitle(title);
        setSectionID(sectionID);
        setImageLink(imageLink);
        setProfileImageLink(profileImageLink);
        setNumLikes(0);
        pinned = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSectionID() {
        return sectionID;
    }

    public void setSectionID(String sectionID) {
        this.sectionID = sectionID;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getProfileImageLink() {
        return profileImageLink;
    }

    public void setProfileImageLink(String profileImageLink) {
        this.profileImageLink = profileImageLink;
    }

    @SuppressWarnings("unused")
    public int getNumLikes() {
        return numLikes;
    }

    @Exclude
    public String getNumLikesString() {
        return (numLikes/1000 > 1) ? numLikes/1000 + "k" : String.valueOf(numLikes);
    }

    public void setNumLikes(int numLikes) {
        this.numLikes = numLikes;
    }

    public boolean getPinned() {
        return pinned;
    }

    @SuppressWarnings("unused")
    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }
}
