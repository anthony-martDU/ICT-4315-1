package com.example.parkingsystem;

import java.util.Properties;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegisterCarCommandTest {
    @Test
    void commandMetadataAndExecuteRegisterCar() {
        ParkingOffice office = new ParkingOffice();
        Customer owner = new Customer(
                "customer-1", "John", "Smith", "555-0100", null);
        office.register(owner);
        RegisterCarCommand command = new RegisterCarCommand(office);
        Properties params = new Properties();
        params.setProperty("licensePlate", "ABC-123");
        params.setProperty("type", "SUV");
        params.setProperty("customerId", owner.getId());

        String permitId = command.execute(params);

        assertEquals("CAR", command.getCommandName());
        assertEquals("Register Car", command.getDisplayName());
        assertEquals("ABC-123", office.getCar(permitId).getLicensePlate());
        assertEquals(owner, office.getCar(permitId).getOwner());
    }

    @Test
    void executeRejectsUnknownCustomer() {
        RegisterCarCommand command = new RegisterCarCommand(new ParkingOffice());
        Properties params = new Properties();
        params.setProperty("licensePlate", "ABC-123");
        params.setProperty("type", "SUV");
        params.setProperty("customerId", "customer-1");

        assertThrows(IllegalArgumentException.class, () -> command.execute(params));
    }

    @Test
    void executeRejectsInvalidCarType() {
        ParkingOffice office = new ParkingOffice();
        Customer owner = new Customer(
                "customer-1", "John", "Smith", "555-0100", null);
        office.register(owner);
        RegisterCarCommand command = new RegisterCarCommand(office);
        Properties params = new Properties();
        params.setProperty("licensePlate", "ABC-123");
        params.setProperty("type", "TRUCK");
        params.setProperty("customerId", owner.getId());

        assertThrows(IllegalArgumentException.class, () -> command.execute(params));
    }
}