package com.eventbooking.member5.controller;

import com.eventbooking.model.CardPayment;
import com.eventbooking.model.CashPayment;
import com.eventbooking.model.Payment;
import com.eventbooking.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<?> getPaymentById(@PathVariable String paymentId) {
        Payment payment = paymentService.getPaymentById(paymentId);
        if (payment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Payment record not found."));
        }
        return ResponseEntity.ok(payment);
    }

    @PostMapping
    public ResponseEntity<?> addPayment(@RequestBody PaymentRequest request) {
        try {
            Payment payment = paymentService.addPayment(toPayment(request));
            return ResponseEntity.status(HttpStatus.CREATED).body(payment);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{paymentId}")
    public ResponseEntity<?> updatePayment(@PathVariable String paymentId, @RequestBody PaymentRequest request) {
        try {
            Payment payment = paymentService.updatePayment(paymentId, toPayment(request));
            return ResponseEntity.ok(payment);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<?> deletePayment(@PathVariable String paymentId) {
        try {
            paymentService.deletePayment(paymentId);
            return ResponseEntity.ok(Map.of("message", "Payment record deleted successfully."));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    private Payment toPayment(PaymentRequest request) {
        String paymentType = request.getPaymentType() == null ? "" : request.getPaymentType().toUpperCase();
        if ("CARD".equals(paymentType)) {
            return new CardPayment(request.getPaymentId(), request.getCustomerName(), request.getBookingId(),
                    request.getAmount(), request.getPaymentDate(), request.getPaymentStatus(),
                    request.getCardHolderName(), request.getCardNumber(), request.getExpiryDate(),
                    request.getCvv(), request.getCardType());
        }
        if ("CASH".equals(paymentType)) {
            return new CashPayment(request.getPaymentId(), request.getCustomerName(), request.getBookingId(),
                    request.getAmount(), request.getPaymentDate(), request.getPaymentStatus(),
                    request.getReceiptNumber(), request.getPaidLocation());
        }
        throw new IllegalArgumentException("Payment type must be either CARD or CASH.");
    }

    public static class PaymentRequest {
        private String paymentId;
        private String customerName;
        private String bookingId;
        private String paymentType;
        private double amount;
        private String paymentDate;
        private String paymentStatus;
        private String cardHolderName;
        private String cardNumber;
        private String expiryDate;
        private String cvv;
        private String cardType;
        private String receiptNumber;
        private String paidLocation;

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
    }
}
