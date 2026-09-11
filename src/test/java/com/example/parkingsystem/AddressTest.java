package com.example.parkingsystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddressTest {
    @Test
    void getAddressInfoReturnsFormattedAddress() {
        Address address = new Address(
                "123 Main St", "Apt 4", "Springfield", "IL", "62701");

        assertEquals(
                "123 Main St, Apt 4, Springfield, IL 62701",
                address.getAddressInfo());
    }
}