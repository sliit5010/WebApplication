package com.eventbooking.member5.model;

public class Booking extends AbstractBooking {

    public Booking() {}

    public Booking(String id, String customerName, String customerEmail, String packageId, String eventDate) {
        this.id = id;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.packageId = packageId;
        this.eventDate = eventDate;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getPackageId() { return packageId; }
    public void setPackageId(String packageId) { this.packageId = packageId; }

    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }

    @Override
    public String getBookingDetails() {
        return "Booking ID: " + id + ", Customer: " + customerName + ", Date: " + eventDate;
    }
    
    @Override
    public String toString() {
        return id + "|" + customerName + "|" + customerEmail + "|" + packageId + "|" + eventDate;
    }
    
    public static Booking fromString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length == 5) {
            return new Booking(parts[0], parts[1], parts[2], parts[3], parts[4]);
        }
        return null;
    }
}
