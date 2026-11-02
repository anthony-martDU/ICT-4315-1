package com.example.parkingsystem.protocol;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParkingResponseTest {
    @Test
    void storesAndPrintsStatusAndMessage() {
        ParkingResponse response = new ParkingResponse(200, "customer-1");

        assertEquals(200, response.getStatusCode());
        assertEquals("customer-1", response.getMessage());
        assertEquals("ParkingResponse{statusCode=200, message='customer-1'}",
                response.toString());
    }

    @Test
    void convertsToAndFromJson() {
        ParkingResponse response = new ParkingResponse(400,
                "Missing parameter: name");

        ParkingResponse restored = ParkingResponse.fromJson(response.toJson());

        assertEquals(400, restored.getStatusCode());
        assertEquals("Missing parameter: name", restored.getMessage());
    }
}