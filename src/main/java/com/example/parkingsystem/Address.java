package com.example.parkingsystem;

public class Address {
    private final String streetAddress1;
    private final String streetAddress2;
    private final String city;
    private final String state;
    private final String zip;

    public Address(String streetAddress1, String streetAddress2, String city,
                   String state, String zip) {
        this.streetAddress1 = streetAddress1;
        this.streetAddress2 = streetAddress2;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public String getAddressInfo() {
        return streetAddress1 + ", " + streetAddress2 + ", " + city + ", "
                + state + " " + zip;
    }
}