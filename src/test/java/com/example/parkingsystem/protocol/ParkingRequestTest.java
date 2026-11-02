package com.example.parkingsystem.protocol;

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParkingRequestTest {
    @Test
    void storesAndPrintsCommandAndParameters() {
        Properties parameters = new Properties();
        parameters.setProperty("name", "Rob");
        ParkingRequest request = new ParkingRequest("CUSTOMER", parameters);

        assertEquals("CUSTOMER", request.getCommandName());
        assertEquals("Rob", request.getParameters().getProperty("name"));
        assertEquals("ParkingRequest{commandName='CUSTOMER', parameters={name=Rob}}",
                request.toString());
    }

    @Test
    void convertsToAndFromJson() {
        Properties parameters = new Properties();
        parameters.setProperty("name", "Rob Smith");
        parameters.setProperty("address", "123 Main St");
        ParkingRequest request = new ParkingRequest("CUSTOMER", parameters);

        ParkingRequest restored = ParkingRequest.fromJson(request.toJson());

        assertEquals("CUSTOMER", restored.getCommandName());
        assertEquals("Rob Smith", restored.getParameters().getProperty("name"));
        assertEquals("123 Main St", restored.getParameters().getProperty("address"));
    }
}