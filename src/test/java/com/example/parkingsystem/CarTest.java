package com.example.parkingsystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CarTest {
    @Test
    void carExposesItsTypeLicensePlateAndOwner() {
        Customer owner = new Customer(
                "customer-1", "John", "Smith", "555-0100", null);
        Car car = new Car(CarType.SUV, "ABC-123", owner);

        assertEquals(CarType.SUV, car.getType());
        assertEquals("ABC-123", car.getLicensePlate());
        assertEquals(owner, car.getOwner());
    }
}