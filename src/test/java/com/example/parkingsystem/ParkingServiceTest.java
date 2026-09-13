package com.example.parkingsystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParkingServiceTest {
    @Test
    void customerCommandRegistersCustomerAndReturnsCustomerId() {
        ParkingOffice office = new ParkingOffice();
        ParkingService service = new ParkingService(office);

        String customerId = service.performCommand("CUSTOMER", new String[]{
                "name=John Smith",
                "address=123 Main St",
                "phoneNumber=555-0100"
        });

        assertEquals("customer-1", customerId);
        assertEquals(customerId, office.getCustomer(customerId).getId());
        assertEquals("John Smith", office.getCustomer(customerId).getCustomerName());
    }

    @Test
    void carCommandRegistersCarAndReturnsPermitId() {
        ParkingOffice office = new ParkingOffice();
        ParkingService service = new ParkingService(office);
        String customerId = service.performCommand("CUSTOMER", new String[]{
                "name=John Smith", "address=123 Main St"
        });

        String permitId = service.performCommand("CAR", new String[]{
                "licensePlate=ABC-123", "type=SUV", "customerId=" + customerId
        });

        assertEquals("permit-1", permitId);
        assertEquals("ABC-123", office.getCar(permitId).getLicensePlate());
        assertEquals(CarType.SUV, office.getCar(permitId).getType());
    }

    @Test
    void unknownCommandIsRejected() {
        ParkingService service = new ParkingService(new ParkingOffice());

        assertThrows(IllegalArgumentException.class,
                () -> service.performCommand("UNKNOWN", new String[0]));
    }

    @Test
    void parameterValuesCanContainEquals() {
        ParkingOffice office = new ParkingOffice();
        ParkingService service = new ParkingService(office);

        String customerId = service.performCommand("CUSTOMER", new String[]{
                "name=John Smith", "address=unit=4"
        });

        assertEquals("customer-1", customerId);
    }
}