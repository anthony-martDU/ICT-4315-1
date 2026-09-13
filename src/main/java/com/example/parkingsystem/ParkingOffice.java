package com.example.parkingsystem;

import java.util.LinkedHashMap;
import java.util.Map;

public class ParkingOffice {
    private final Map<String, Customer> customers = new LinkedHashMap<>();
    private final Map<String, Car> cars = new LinkedHashMap<>();
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
        return permitId;
    }

    public Customer getCustomer(String customerId) {
        return customers.get(customerId);
    }

    public Car getCar(String permitId) {
        return cars.get(permitId);
    }
}