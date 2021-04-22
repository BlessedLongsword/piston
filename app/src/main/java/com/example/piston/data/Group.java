package com.example.piston.data;

import java.util.ArrayList;

public class Group extends Section {

    private ArrayList<String> members;
    private String id;

    public Group() {
    }

    public Group(String name, String description, String id) {
        super(name, description);
        members = new ArrayList<>();
        this.id = id;
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
}
