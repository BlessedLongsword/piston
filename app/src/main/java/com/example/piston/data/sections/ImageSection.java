package com.example.piston.data.sections;

public abstract class ImageSection extends Section {

    private String imageLink;

    public ImageSection() {}

    public ImageSection(String id, String title, String description, String imageLink) {
        super(id, title, description);
        setImageLink(imageLink);
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

}
