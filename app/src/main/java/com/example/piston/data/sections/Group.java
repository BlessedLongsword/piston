package com.example.piston.data.sections;

public class Group extends ImageSection {

    private int numMembers;
    private boolean modMode;

    public Group() {}

    public Group(String id, String name, String description, String imageLink) {
        super(id, name, description, imageLink);
        setNumMembers(1);
        setModMode(false);
    }

    public int getNumMembers() {
        return numMembers;
    }

    public void setNumMembers(int numMembers) {
        this.numMembers = numMembers;
    }

    public boolean getModMode() {
        return modMode;
    }

    public void setModMode(boolean modMode) {
        this.modMode = modMode;
    }
}
