package com.eventbooking.model;

public class Admin extends User {
    private String accessLevel;

    public Admin(String userId, String name, String email, String password, String accessLevel) {
        super(userId, name, email, password, "Admin");
        this.accessLevel = accessLevel;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    @Override
    public String toFileFormat() {
        return super.toFileFormat() + "," + accessLevel;
    }
}
