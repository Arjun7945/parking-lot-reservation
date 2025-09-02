package com.testproject.parking_lot_reservation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ParkingLotReservationApplicationTests {

    @Test
    void contextLoads() {
        // This test will pass if the application context loads successfully
    }
}