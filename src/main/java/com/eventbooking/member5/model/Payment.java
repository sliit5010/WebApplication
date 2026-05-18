package com.eventbooking.member5.model;

public abstract class Payment {
    protected String paymentId;
    protected String customerName;
    protected String bookingId;
    protected String paymentType;
    protected double amount;
    protected String paymentDate;
    protected String paymentStatus;

    public Payment() {
    }

    public Payment(String paymentId, String customerName, String bookingId, String paymentType,
                   double amount, String paymentDate, String paymentStatus) {
        this.paymentId = paymentId;
        this.customerName = customerName;
        this.bookingId = bookingId;
        this.paymentType = paymentType;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    protected String baseFileFormat() {
        return clean(paymentId) + "|" + clean(customerName) + "|" + clean(bookingId) + "|"
                + clean(paymentType) + "|" + amount + "|" + clean(paymentDate) + "|"
                + clean(paymentStatus);
    }

    protected String clean(String value) {
        return value == null ? "" : value.replace("|", " ").trim();
    }

    public abstract String toFileFormat();
}
