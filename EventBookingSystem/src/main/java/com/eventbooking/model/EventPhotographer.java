package com.eventbooking.model;

public class EventPhotographer extends Photographer {
    private boolean sameDayEdit; // Specific attribute for corporate/parties

    public EventPhotographer(String id, String name, String email, String phone, boolean sameDayEdit) {
        super(id, name, email, phone, "Event");
        this.sameDayEdit = sameDayEdit;
    }

    @Override
    public String toFileFormat() {
        return super.toFileFormat() + "," + sameDayEdit;
    }
}
