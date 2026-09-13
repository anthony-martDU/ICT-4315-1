package com.example.parkingsystem;

import java.util.Properties;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegisterCustomerCommandTest {
    @Test
    void commandMetadataAndExecuteRegisterCustomer() {
        ParkingOffice office = new ParkingOffice();
        RegisterCustomerCommand command = new RegisterCustomerCommand(office);
        Properties params = new Properties();
        params.setProperty("name", "John Smith");
        params.setProperty("address", "123 Main St");
        params.setProperty("phoneNumber", "555-0100");

        String customerId = command.execute(params);

        assertEquals("CUSTOMER", command.getCommandName());
        assertEquals("Register Customer", command.getDisplayName());
        assertEquals("John Smith", office.getCustomer(customerId).getCustomerName());
    }

    @Test
    void executeRejectsMissingName() {
        RegisterCustomerCommand command = new RegisterCustomerCommand(
                new ParkingOffice());
        Properties params = new Properties();
        params.setProperty("address", "123 Main St");

        assertThrows(IllegalArgumentException.class, () -> command.execute(params));
    }
}