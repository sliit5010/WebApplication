package com.eventbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private UserService userService;

    @Autowired
    private PhotographerService photographerService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private PackageService packageService;

    @Autowired
    private PaymentService paymentService;

    public Map<String, Object> getSystemStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // Count from files if service doesn't have getAll()
        int totalUsers = countLines("users.txt");
        int totalPhotographers = countLines("photographers.txt");
        
        stats.put("totalUsers", totalUsers);
        stats.put("totalPhotographers", totalPhotographers);
        stats.put("totalBookings", bookingService.getAllBookings().size());
        stats.put("totalPackages", packageService.getAllPackages().size());
        
        double totalRevenue = paymentService.getAllPayments().stream()
                .filter(p -> "COMPLETED".equals(p.getPaymentStatus()))
                .mapToDouble(p -> p.getAmount())
                .sum();
        stats.put("totalRevenue", totalRevenue);
        
        return stats;
    }

    private int countLines(String filename) {
        int count = 0;
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(filename))) {
            while (reader.readLine() != null) count++;
        } catch (Exception e) {
            // File might not exist yet
        }
        return count;
    }
}
