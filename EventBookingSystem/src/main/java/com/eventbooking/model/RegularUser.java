package com.eventbooking.model;

public class RegularUser extends User {
    public RegularUser(String userId, String name, String email, String password) {
        super(userId, name, email, password, "Regular");
    }
}
