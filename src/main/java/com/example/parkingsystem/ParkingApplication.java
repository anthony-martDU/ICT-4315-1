package com.example.parkingsystem;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class ParkingApplication {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ParkingSystemModule());
        ParkingService service = injector.getInstance(ParkingService.class);

        String customerId = service.performCommand("CUSTOMER", new String[]{
                "name=John Smith",
                "address=123 Main St",
                "phoneNumber=555-0100"
        });

        System.out.println("Customer registered successfully: " + customerId);

        String permitId = service.performCommand("CAR", new String[]{
                "licensePlate=ABC-123",
                "type=SUV",
                "customerId=" + customerId
        });

        System.out.println("Car registered successfully; permit issued: " + permitId);
    }
}