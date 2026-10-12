package com.example.parkingsystem;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ParkingOffice {
    private final Map<String, Customer> customers = new LinkedHashMap<>();
    private final Map<String, Car> cars = new LinkedHashMap<>();
    private final Map<String, ParkingPermit> permits = new LinkedHashMap<>();
    private final List<ParkingLot> parkingLots = new ArrayList<>();
    private int nextCustomerNumber = 1;
    private int nextPermitNumber = 1;

    public String register(Customer customer) {
        String customerId = "customer-" + nextCustomerNumber++;
        customer.assignId(customerId);
        customers.put(customerId, customer);
        return customerId;
    }

    public String register(Car car) {
        String permitId = "permit-" + nextPermitNumber++;
        cars.put(permitId, car);
        permits.put(permitId, new ParkingPermit(permitId, car));
        return permitId;
    }

    public Customer getCustomer(String customerId) {
        return customers.get(customerId);
    }

    public Car getCar(String permitId) {
        return cars.get(permitId);
    }

    public ParkingPermit getPermit(String permitId) {
        return permits.get(permitId);
    }

    public void addParkingLot(ParkingLot parkingLot) {
        parkingLots.add(Objects.requireNonNull(parkingLot, "parkingLot"));
    }

    public List<ParkingLot> getParkingLots() {
        return Collections.unmodifiableList(parkingLots);
    }
}