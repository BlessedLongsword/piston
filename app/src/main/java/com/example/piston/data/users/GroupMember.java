package com.example.piston.data.users;

public class GroupMember extends User {

    public static final int OWNER = 0;
    public static final int MOD = 1;

    private int priority;

    public GroupMember() {}

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
