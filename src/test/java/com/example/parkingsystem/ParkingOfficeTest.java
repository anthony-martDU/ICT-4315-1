package com.example.parkingsystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ParkingOfficeTest {
    @Test
    void registerCustomerAssignsAndStoresCustomerId() {
        ParkingOffice office = new ParkingOffice();
        Customer customer = new Customer(
                "", "John", "Smith", "555-0100", null);

        String customerId = office.register(customer);

        assertEquals("customer-1", customerId);
        assertEquals(customerId, customer.getId());
        assertSame(customer, office.getCustomer(customerId));
    }

    @Test
    void registerCarCreatesPermitAndStoresCar() {
        ParkingOffice office = new ParkingOffice();
        Customer owner = new Customer(
                "customer-1", "John", "Smith", "555-0100", null);
        Car car = new Car(CarType.COMPACT, "ABC-123", owner);

        String permitId = office.register(car);

        assertEquals("permit-1", permitId);
        assertSame(car, office.getCar(permitId));
    }
}