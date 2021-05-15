package com.example.piston.data.sections;

public class Category extends ImageSection {

    private boolean nsfw;

    public Category() {}

    public Category(String id, String title, String description, String imageLink, boolean nsfw) {
        super(id, title, description, imageLink);
    }

    public boolean isNsfw() {
        return nsfw;
    }

    public void setNsfw(boolean nsfw) {
        this.nsfw = nsfw;
    }
}
