package com.eventbooking.model;

/**
 * Abstract base class representing a system User.
 * Demonstrates OOP: Encapsulation (private fields + getters/setters)
 *                   Inheritance (extended by RegularUser and PremiumUser)
 */
public abstract class User {
    private String userId;
    private String name;
    private String email;
    private String password;
    private String userType;

    // Encapsulation: all fields are private/protected
    protected String userId;
    protected String name;
    protected String email;
    protected String password;
    protected String userType;

    // Constructor
    public User(String userId, String name, String email, String password, String userType) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    // --- Getters & Setters (Encapsulation) ---

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    /**
     * Converts the user object into a CSV line for storage in users.txt
     * Format: userId,name,email,password,userType
     */
    public String toFileFormat() {
        return userId + "," + name + "," + email + "," + password + "," + userType;
    }

    @Override
    public String toString() {
        return "User{userId='" + userId + "', name='" + name + "', email='" + email + "', userType='" + userType + "'}";
    }
}