package com.eventbooking.model;

/**
 * Represents a Regular (standard) user of the system.
 * Demonstrates OOP: Inheritance — extends User base class.
 */
public class RegularUser extends User {

    public RegularUser(String userId, String name, String email, String password) {
        // Calls the User superclass constructor; userType is fixed as "Regular"
        super(userId, name, email, password, "Regular");
    }
}