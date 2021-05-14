package com.example.piston.data;

public class Category extends Section {

    private boolean nsfw;
    private String imageID, imageLink, id;

    public Category() {}

    public Category(String title, String description, boolean nsfw, String imageID, String imageLink, String id) {
        super(title, description);
        this.nsfw = nsfw;
        this.imageID = imageID;
        this.imageLink = imageLink;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean getIsNSFW() {
        return nsfw;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getImageID() {
        return imageID;
    }
}
