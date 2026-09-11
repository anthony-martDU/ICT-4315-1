package com.example.parkingsystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerTest {
    @Test
    void getCustomerNameReturnsFirstAndLastName() {
        Address address = new Address(
                "123 Main St", "Apt 4", "Springfield", "IL", "62701");
        Customer customer = new Customer(
                "customer-1", "John", "Smith", "555-0100", address);

        assertEquals("John Smith", customer.getCustomerName());
    }
}