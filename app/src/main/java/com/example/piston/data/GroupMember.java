package com.example.piston.data;

public class GroupMember extends User {

    boolean mod, owner;

    public GroupMember() {
    }

    public boolean getIsMod() {
        return mod;
    }

    public void getSetMod(boolean mod) {
        this.mod = mod;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }
}
