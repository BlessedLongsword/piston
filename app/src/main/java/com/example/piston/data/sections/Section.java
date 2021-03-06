package com.example.piston.data.sections;

public abstract class Section {

    private String id, title, description;

    public Section() {}

    public Section(String id, String title, String description)  {
        setId(id);
        setTitle(title);
        setDescription(description);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
