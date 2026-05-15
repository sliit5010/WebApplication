package com.eventbooking.model;

public class PremiumUser extends User {
    private String discountCode;

    public PremiumUser(String userId, String name, String email, String password, String discountCode) {
        super(userId, name, email, password, "Premium");
        this.discountCode = discountCode;
    }

    @Override
    public String toFileFormat() {
        return super.toFileFormat() + "," + discountCode;
    }
}