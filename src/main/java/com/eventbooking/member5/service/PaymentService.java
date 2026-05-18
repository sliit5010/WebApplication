package com.eventbooking.member5.service;

import com.eventbooking.model.CardPayment;
import com.eventbooking.model.CashPayment;
import com.eventbooking.model.Payment;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {
    private static final String FILE_PATH = "payments.txt";

    public PaymentService() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new IllegalStateException("Unable to create payments.txt", e);
            }
        }
    }

    public Payment addPayment(Payment payment) {
        validatePayment(payment);
        if (isBlank(payment.getPaymentId())) {
            payment.setPaymentId(UUID.randomUUID().toString());
        }
        appendToFile(payment);
        return payment;
    }

    public List<Payment> getAllPayments() {
        return loadFromFile();
    }

    public Payment getPaymentById(String paymentId) {
        return getAllPayments().stream()
                .filter(payment -> payment.getPaymentId().equals(paymentId))
                .findFirst()
                .orElse(null);
    }

    public Payment updatePayment(String paymentId, Payment updatedPayment) {
        validatePayment(updatedPayment);
        updatedPayment.setPaymentId(paymentId);

        List<Payment> payments = getAllPayments();
        boolean found = false;
        for (int i = 0; i < payments.size(); i++) {
            if (payments.get(i).getPaymentId().equals(paymentId)) {
                payments.set(i, updatedPayment);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IllegalArgumentException("Payment record not found.");
        }
        saveToFile(payments);
        return updatedPayment;
    }

    public void deletePayment(String paymentId) {
        List<Payment> payments = getAllPayments();
        boolean removed = payments.removeIf(payment -> payment.getPaymentId().equals(paymentId));
        if (!removed) {
            throw new IllegalArgumentException("Payment record not found.");
        }
        saveToFile(payments);
    }

    public void saveToFile(List<Payment> payments) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Payment payment : payments) {
                writer.write(payment.toFileFormat());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save payment records.", e);
        }
    }

    public List<Payment> loadFromFile() {
        List<Payment> payments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Payment payment = parsePayment(line);
                if (payment != null) {
                    payments.add(payment);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load payment records.", e);
        }
        return payments;
    }

    private void appendToFile(Payment payment) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(payment.toFileFormat());
            writer.newLine();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save payment record.", e);
        }
    }

    private Payment parsePayment(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 8) {
            return null;
        }

        String paymentId = parts[0];
        String customerName = parts[1];
        String bookingId = parts[2];
        String paymentType = parts[3];
        double amount;
        try {
            amount = Double.parseDouble(parts[4]);
        } catch (NumberFormatException e) {
            return null;
        }
        String paymentDate = parts[5];
        String paymentStatus = parts[6];

        if ("CARD".equalsIgnoreCase(paymentType) && parts.length >= 12) {
            return new CardPayment(paymentId, customerName, bookingId, amount, paymentDate,
                    paymentStatus, parts[7], parts[8], parts[9], parts[10], parts[11]);
        }
        if ("CASH".equalsIgnoreCase(paymentType) && parts.length >= 9) {
            return new CashPayment(paymentId, customerName, bookingId, amount, paymentDate,
                    paymentStatus, parts[7], parts[8]);
        }
        return null;
    }

    private void validatePayment(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment details are required.");
        }
        if (isBlank(payment.getCustomerName())) {
            throw new IllegalArgumentException("Customer name cannot be empty.");
        }
        if (isBlank(payment.getBookingId())) {
            throw new IllegalArgumentException("Booking ID cannot be empty.");
        }
        if (payment.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }
        if (isBlank(payment.getPaymentDate())) {
            payment.setPaymentDate(LocalDate.now().toString());
        }
        if (!"CARD".equalsIgnoreCase(payment.getPaymentType()) && !"CASH".equalsIgnoreCase(payment.getPaymentType())) {
            throw new IllegalArgumentException("Payment type must be either CARD or CASH.");
        }
        payment.setPaymentType(payment.getPaymentType().toUpperCase());

        if (!"PENDING".equalsIgnoreCase(payment.getPaymentStatus())
                && !"COMPLETED".equalsIgnoreCase(payment.getPaymentStatus())
                && !"FAILED".equalsIgnoreCase(payment.getPaymentStatus())) {
            throw new IllegalArgumentException("Payment status must be PENDING, COMPLETED, or FAILED.");
        }
        payment.setPaymentStatus(payment.getPaymentStatus().toUpperCase());

        if (payment instanceof CardPayment cardPayment) {
            validateCardPayment(cardPayment);
        } else if (payment instanceof CashPayment cashPayment) {
            validateCashPayment(cashPayment);
        } else {
            throw new IllegalArgumentException("Payment must be a card or cash payment.");
        }
    }

    private void validateCardPayment(CardPayment payment) {
        if (isBlank(payment.getCardHolderName())) {
            throw new IllegalArgumentException("Card holder name must not be empty.");
        }
        if (payment.getCardNumber() == null || !payment.getCardNumber().matches("\\d{16}")) {
            throw new IllegalArgumentException("Card number must contain exactly 16 digits.");
        }
        if (isBlank(payment.getExpiryDate())) {
            throw new IllegalArgumentException("Expiry date must not be empty.");
        }
        if (payment.getCvv() == null || !payment.getCvv().matches("\\d{3}")) {
            throw new IllegalArgumentException("CVV must contain exactly 3 digits.");
        }
    }

    private void validateCashPayment(CashPayment payment) {
        if (isBlank(payment.getReceiptNumber())) {
            throw new IllegalArgumentException("Receipt number must not be empty for cash payment.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
