package com.eventbooking;

import com.eventbooking.BookingSystemApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = BookingSystemApplication.class, properties = "debug=false")
class BookingSystemApplicationTests {

    @Test
    void contextLoads() {
    }

}
