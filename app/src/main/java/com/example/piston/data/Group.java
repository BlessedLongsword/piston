package com.example.piston.data;

import java.util.ArrayList;

public class Group extends Section {

    private ArrayList<String> members, mods;
    private String id, imageID, imageLink;

    public Group() {
    }

    public Group(String name, String description, String id, String imageID, String imageLink) {
        super(name, description);
        members = new ArrayList<>();
        mods = new ArrayList<>();
        this.id = id;
        this.imageID = imageID;
        this.imageLink = imageLink;
    }

    public Group(String name, String description, ArrayList<String> members) {
        super(name, description);
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getMods() {
        return mods;
    }

    public void setMods(ArrayList<String> mods) {
        this.mods = mods;
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
