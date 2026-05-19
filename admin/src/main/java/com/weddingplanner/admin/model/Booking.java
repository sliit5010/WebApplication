package com.weddingplanner.admin.model;

import java.time.LocalDate;

public class Booking {
    private String id;
    private String userId;
    private String photographerId;
    private String packageId;
    private LocalDate bookingDate;
    private String status;

    public Booking() {}

    public Booking(String id, String userId, String photographerId, String packageId, LocalDate bookingDate, String status) {
        this.id = id;
        this.userId = userId;
        this.photographerId = photographerId;
        this.packageId = packageId;
        this.bookingDate = bookingDate;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getPhotographerId() { return photographerId; }
    public void setPhotographerId(String photographerId) { this.photographerId = photographerId; }
    public String getPackageId() { return packageId; }
    public void setPackageId(String packageId) { this.packageId = packageId; }
    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
