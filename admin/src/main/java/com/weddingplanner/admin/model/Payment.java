package com.weddingplanner.admin.model;

import java.time.LocalDate;

public class Payment {
    private String id;
    private String bookingId;
    private double amount;
    private LocalDate paymentDate;
    private String method;

    public Payment() {}

    public Payment(String id, String bookingId, double amount, LocalDate paymentDate, String method) {
        this.id = id;
        this.bookingId = bookingId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.method = method;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
}
