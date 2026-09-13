package com.example.parkingsystem;

public class Car {
    private final CarType type;
    private final String licensePlate;
    private final Customer owner;

    public Car(CarType type, String licensePlate, Customer owner) {
        this.type = type;
        this.licensePlate = licensePlate;
        this.owner = owner;
    }

    public CarType getType() {
        return type;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public Customer getOwner() {
        return owner;
    }
}