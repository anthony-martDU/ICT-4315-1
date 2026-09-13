package com.example.parkingsystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ParkingServiceValidationTest {
    @Test
    void malformedParameterIsRejected() {
        ParkingService service = new ParkingService(new ParkingOffice());

        assertThrows(IllegalArgumentException.class,
                () -> service.performCommand("CUSTOMER",
                        new String[]{"name John Smith"}));
    }

    @Test
    void missingCustomerParameterIsRejected() {
        ParkingService service = new ParkingService(new ParkingOffice());

        assertThrows(IllegalArgumentException.class,
                () -> service.performCommand("CUSTOMER",
                        new String[]{"address=123 Main St"}));
    }
}