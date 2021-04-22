package com.example.piston.data;

import java.util.ArrayList;

public class Group extends Section {

    private ArrayList<String> members;

    public Group() {
    }

    public Group(String name, String description) {
        super(name, description);
        members = new ArrayList<>();
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
}
