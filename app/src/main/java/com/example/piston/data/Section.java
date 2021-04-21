package com.example.piston.data;

public class Section {

    private String title, description;

    public Section() {}

    public Section(String title, String description)  {
        setTitle(title);
        setDescription(description);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
