package com.example.parkingsystem;

import java.util.Objects;

public final class ParkingPermit {
    private final String id;
    private final Car car;

    public ParkingPermit(String id, Car car) {
        this.id = Objects.requireNonNull(id, "id");
        this.car = Objects.requireNonNull(car, "car");
    }

    public String getId() {
        return id;
    }

    public Car getCar() {
        return car;
    }

    public Customer getOwner() {
        return car.getOwner();
    }
}