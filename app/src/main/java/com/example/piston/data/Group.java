package com.example.piston.data;

public class Group extends Section {

    private String imageID, imageLink;

    public Group() {}

    public Group(String name, String description, String id, String imageID, String imageLink) {
        super(id, name, description);
        this.imageID = imageID;
        this.imageLink = imageLink;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
