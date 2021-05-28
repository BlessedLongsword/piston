package com.example.piston.data.sections;

public class Category extends ImageSection {

    private boolean nsfw;
    private int numSubs;

    public Category() {}

    public Category(String id, String title, String description, String imageLink, boolean nsfw) {
        super(id, title, description, imageLink);
        setNsfw(nsfw);
        setNumSubs(0);
    }

    @SuppressWarnings("unused")
    public boolean isNsfw() {
        return nsfw;
    }

    public void setNsfw(boolean nsfw) {
        this.nsfw = nsfw;
    }

    @SuppressWarnings("unused")
    public int getNumSubs() {
        return numSubs;
    }

    public void setNumSubs(int numSubs) {
        this.numSubs = numSubs;
    }
}
