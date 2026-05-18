package com.eventbooking.member5.model;

import java.io.Serializable;

// OOP: Abstraction & Association.
// Association: Booking "has a" Package via packageId or Package object reference.
// Abstract class defines the core booking behavior.
public abstract class AbstractBooking implements Serializable {
    protected String id;
    protected String customerName;
    protected String customerEmail;
    protected String packageId; // Connected to member4's Package
    protected String eventDate;
    
    // Abstract methods to be implemented
    public abstract String getBookingDetails();
}
