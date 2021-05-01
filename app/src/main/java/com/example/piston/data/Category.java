package com.example.piston.data;

public class Category extends Section {

    boolean nsfw;
    private String imageID;
    private String imageLink;

    public Category() {}

    public Category(String title, String description, boolean nsfw, String imageID, String imageLink) {
        super(title, description);
        this.nsfw = nsfw;
        this.imageID = imageID;
        this.imageLink = imageLink;
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
