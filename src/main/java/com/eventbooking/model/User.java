package com.eventbooking.model;

public abstract class User {
    private String userId;
    private String name;
    private String email;
    private String password;
    private String userType;

    public User(String userId, String name, String email, String password, String userType) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getUserType() { return userType; }

    public String toFileFormat() {
        return userId + "," + name + "," + email + "," + password + "," + userType;
    }
}