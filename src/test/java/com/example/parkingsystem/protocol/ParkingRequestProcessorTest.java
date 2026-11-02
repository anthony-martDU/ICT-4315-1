package com.example.parkingsystem.protocol;

import com.example.parkingsystem.ParkingOffice;
import com.example.parkingsystem.ParkingService;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParkingRequestProcessorTest {
    @Test
    void processesCustomerAndCarRequests() {
        ParkingRequestProcessor processor = new ParkingRequestProcessor(
                new ParkingService(new ParkingOffice()));

        Properties customerParameters = new Properties();
        customerParameters.setProperty("name", "Rob Smith");
        customerParameters.setProperty("address", "123 Main St");
        ParkingResponse customerResponse = processor.process(
                new ParkingRequest("CUSTOMER", customerParameters).toJson());

        assertEquals(200, customerResponse.getStatusCode());
        assertEquals("customer-1", customerResponse.getMessage());

        Properties carParameters = new Properties();
        carParameters.setProperty("licensePlate", "ROB4CO");
        carParameters.setProperty("type", "SUV");
        carParameters.setProperty("customerId", customerResponse.getMessage());
        ParkingResponse carResponse = processor.process(
                new ParkingRequest("CAR", carParameters).toJson());

        assertEquals(200, carResponse.getStatusCode());
        assertEquals("permit-1", carResponse.getMessage());
    }

    @Test
    void returnsJsonCompatibleErrorForInvalidRequest() {
        ParkingRequestProcessor processor = new ParkingRequestProcessor(
                new ParkingService(new ParkingOffice()));

        ParkingResponse response = processor.process("not-json");

        assertEquals(400, response.getStatusCode());
        assertEquals("Invalid request JSON", response.getMessage());
    }
}