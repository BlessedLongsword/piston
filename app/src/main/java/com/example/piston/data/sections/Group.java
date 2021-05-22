package com.example.piston.data.sections;

public class Group extends ImageSection {

    private int numMembers;

    public Group() {}

    public Group(String id, String name, String description, String imageLink) {
        super(id, name, description, imageLink);
        setNumMembers(1);
    }

    public int getNumMembers() {
        return numMembers;
    }

    public void setNumMembers(int numMembers) {
        this.numMembers = numMembers;
    }
}
