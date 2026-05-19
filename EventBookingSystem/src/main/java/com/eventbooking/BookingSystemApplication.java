package com.eventbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookingSystemApplication {
    public static void main(String[] args) {
        System.setProperty("debug", "false");
        SpringApplication.run(BookingSystemApplication.class, args);
    }
}
