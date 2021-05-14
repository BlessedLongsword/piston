package com.example.piston.data;

public class GroupMember extends User {

    public static final int OWNER = 0;
    public static final int MOD = 1;

    private int priority;

    public GroupMember() {}

    public GroupMember(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

}