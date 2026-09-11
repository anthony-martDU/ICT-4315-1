package com.example.parkingsystem;

public class Customer {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final Address address;

    public Customer(String id, String firstName, String lastName,
                    String phoneNumber, Address address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getCustomerName() {
        return firstName + " " + lastName;
    }
}