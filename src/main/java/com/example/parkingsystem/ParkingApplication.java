package com.example.parkingsystem;

public class ParkingApplication {
    public static void main(String[] args) {
        ParkingOffice office = new ParkingOffice();
        ParkingService service = new ParkingService(office);

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