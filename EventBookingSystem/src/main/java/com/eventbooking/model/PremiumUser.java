package com.eventbooking.model;

/**
 * Represents a Premium user of the system.
 * Demonstrates OOP: Inheritance — extends User base class.
 * Adds an extra field: discountCode, which is exclusive to Premium users.
 */
public class PremiumUser extends User {

    // Extra field specific to PremiumUser (Encapsulation)
    private String discountCode;

    public PremiumUser(String userId, String name, String email, String password, String discountCode) {
        // Calls the User superclass constructor; userType is fixed as "Premium"
        super(userId, name, email, password, "Premium");
        this.discountCode = discountCode;
    }

    public String getDiscountCode() { return discountCode; }
    public void setDiscountCode(String discountCode) { this.discountCode = discountCode; }

    /**
     * Overrides toFileFormat() to include the discountCode.
     * Format: userId,name,email,password,Premium,discountCode
     */
    @Override
    public String toFileFormat() {
        return super.toFileFormat() + "," + discountCode;
    }
}