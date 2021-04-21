package com.example.piston.data;

public class Category extends Section {

    boolean nsfw;

    public Category() {}

    public Category(String title, String description, boolean nsfw) {
        super(title, description);
        this.nsfw = nsfw;
    }

    public boolean getIsNSFW() {
        return nsfw;
    }
}
