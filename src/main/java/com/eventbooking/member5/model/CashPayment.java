package com.eventbooking.member5.model;

import com.eventbooking.model.Payment;

public class CashPayment extends Payment {
    private String receiptNumber;
    private String paidLocation;

    public CashPayment() {
        this.paymentType = "CASH";
    }

    public CashPayment(String paymentId, String customerName, String bookingId, double amount,
                       String paymentDate, String paymentStatus, String receiptNumber,
                       String paidLocation) {
        super(paymentId, customerName, bookingId, "CASH", amount, paymentDate, paymentStatus);
        this.receiptNumber = receiptNumber;
        this.paidLocation = paidLocation;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getPaidLocation() {
        return paidLocation;
    }

    public void setPaidLocation(String paidLocation) {
        this.paidLocation = paidLocation;
    }

    @Override
    public String toFileFormat() {
        return baseFileFormat() + "|" + clean(receiptNumber) + "|" + clean(paidLocation);
    }
}
