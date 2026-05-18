package com.eventbooking.member5.model;

import com.eventbooking.model.Payment;

public class CardPayment extends Payment {
    private String cardHolderName;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private String cardType;

    public CardPayment() {
        this.paymentType = "CARD";
    }

    public CardPayment(String paymentId, String customerName, String bookingId, double amount,
                       String paymentDate, String paymentStatus, String cardHolderName,
                       String cardNumber, String expiryDate, String cvv, String cardType) {
        super(paymentId, customerName, bookingId, "CARD", amount, paymentDate, paymentStatus);
        this.cardHolderName = cardHolderName;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.cardType = cardType;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "";
        }
        return "************" + cardNumber.substring(cardNumber.length() - 4);
    }

    @Override
    public String toFileFormat() {
        return baseFileFormat() + "|" + clean(cardHolderName) + "|" + clean(cardNumber) + "|"
                + clean(expiryDate) + "|" + clean(cvv) + "|" + clean(cardType);
    }
}
