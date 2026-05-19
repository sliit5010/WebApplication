package com.eventbooking.member3;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {
    private final String FILE_PATH = "bookings.txt";

    public BookingService() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Booking booking = Booking.fromString(line);
                if (booking != null) bookings.add(booking);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public Booking getBookingById(String id) {
        return getAllBookings().stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addBooking(Booking booking) {
        if (booking.getId() == null || booking.getId().isEmpty()) {
            booking.setId(UUID.randomUUID().toString());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(booking.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateBooking(Booking updatedBooking) {
        List<Booking> bookings = getAllBookings();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Booking b : bookings) {
                if (b.getId().equals(updatedBooking.getId())) {
                    writer.write(updatedBooking.toString());
                } else {
                    writer.write(b.toString());
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancelBooking(String id) {
        List<Booking> bookings = getAllBookings();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Booking b : bookings) {
                if (!b.getId().equals(id)) {
                    writer.write(b.toString());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
