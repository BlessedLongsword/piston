package com.example.piston.data.posts;

public class Post extends OwnedContent {

    private String title, sectionID, imageLink, profileImageLink;
    private int numLikes;

    public Post() {}

    public Post(String id, String owner, String ownerEmail, String content, String title, String sectionID,
                String imageLink, String profileImageLink) {
        super(id, owner, ownerEmail, content);
        setTitle(title);
        setSectionID(sectionID);
        setImageLink(imageLink);
        setProfileImageLink(profileImageLink);
        setNumLikes(0);
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

    public int getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(int numLikes) {
        this.numLikes = numLikes;
    }
}
