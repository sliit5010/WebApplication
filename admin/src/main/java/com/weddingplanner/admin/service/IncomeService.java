package com.weddingplanner.admin.service;

import com.weddingplanner.admin.model.Payment;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IncomeService {
    
    // Mock data for payments
    public List<Payment> getRecentPayments() {
        List<Payment> payments = new ArrayList<>();
        payments.add(new Payment("P1", "B1", 1500.0, LocalDate.now().minusDays(2), "Credit Card"));
        payments.add(new Payment("P2", "B2", 2000.0, LocalDate.now().minusMonths(1), "PayPal"));
        payments.add(new Payment("P3", "B3", 1200.0, LocalDate.now().minusMonths(2), "Bank Transfer"));
        payments.add(new Payment("P4", "B4", 3000.0, LocalDate.now().minusMonths(1).minusDays(5), "Credit Card"));
        return payments;
    }

    public double getTotalIncome() {
        return getRecentPayments().stream().mapToDouble(Payment::getAmount).sum();
    }

    public Map<String, Double> getIncomeByMonth() {
        return getRecentPayments().stream()
                .collect(Collectors.groupingBy(
                        p -> p.getPaymentDate().getMonth().toString(),
                        Collectors.summingDouble(Payment::getAmount)
                ));
    }
}
