package com.eventbooking.member5.controller;

import com.eventbooking.model.Booking;
import com.eventbooking.service.BookingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public Booking getBooking(@PathVariable String id) {
        return bookingService.getBookingById(id);
    }

    @PostMapping
    public Booking addBooking(@RequestBody Booking booking) {
        bookingService.addBooking(booking);
        return booking;
    }

    @PutMapping("/{id}")
    public void updateBooking(@PathVariable String id, @RequestBody Booking booking) {
        booking.setId(id);
        bookingService.updateBooking(booking);
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable String id) {
        bookingService.cancelBooking(id);
    }
}
