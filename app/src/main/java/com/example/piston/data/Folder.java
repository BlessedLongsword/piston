package com.example.piston.data;

public class Folder extends Section {

    private String id;

    public Folder() {}

    public Folder(String title, String description, String id) {
        super(title, description);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
