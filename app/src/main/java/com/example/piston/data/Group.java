package com.example.piston.data;

public class Group extends Section {

    private String id, imageID, imageLink;

    public Group() {
    }

    public Group(String name, String description, String id, String imageID, String imageLink) {
        super(name, description);
        this.id = id;
        this.imageID = imageID;
        this.imageLink = imageLink;
    }

    public Group(String name, String description) {
        super(name, description);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
