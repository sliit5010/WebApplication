package com.weddingplanner.admin.controller;

import com.weddingplanner.admin.model.Booking;
import com.weddingplanner.admin.model.Payment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/integration")
public class IntegrationController {

    @GetMapping("/bookings")
    public List<Booking> getIntegratedBookings() {
        return Arrays.asList(
            new Booking("B1", "U1", "Ph1", "Pkg1", LocalDate.now(), "Confirmed"),
            new Booking("B2", "U2", "Ph2", "Pkg2", LocalDate.now().plusDays(1), "Pending")
        );
    }

    @GetMapping("/payments")
    public List<Payment> getIntegratedPayments() {
        return Arrays.asList(
            new Payment("P1", "B1", 1500.0, LocalDate.now(), "Credit Card"),
            new Payment("P2", "B2", 2000.0, LocalDate.now(), "PayPal")
        );
    }
}
