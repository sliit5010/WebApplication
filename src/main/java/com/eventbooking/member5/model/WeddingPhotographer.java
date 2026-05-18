package com.eventbooking.member5.model;

public class WeddingPhotographer extends Photographer {
    private boolean droneIncluded; // Specific attribute for weddings

    public WeddingPhotographer(String id, String name, String email, String phone, boolean droneIncluded) {
        super(id, name, email, phone, "Wedding");
        this.droneIncluded = droneIncluded;
    }

    @Override
    public String toFileFormat() {
        return super.toFileFormat() + "," + droneIncluded;
    }
}
