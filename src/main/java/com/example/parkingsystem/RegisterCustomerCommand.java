package com.example.parkingsystem;

import java.util.Properties;

public class RegisterCustomerCommand implements Command {
    private final ParkingOffice office;

    public RegisterCustomerCommand(ParkingOffice office) {
        this.office = office;
    }

    @Override
    public String getCommandName() {
        return "CUSTOMER";
    }

    @Override
    public String getDisplayName() {
        return "Register Customer";
    }

    @Override
    public String execute(Properties params) {
        checkParameters(params);

        String[] nameParts = params.getProperty("name").trim().split("\\s+", 2);
        String firstName = nameParts[0];
        String lastName = nameParts.length == 2 ? nameParts[1] : "";
        Address address = new Address(params.getProperty("address"), "", "", "", "");
        Customer customer = new Customer(
                "", firstName, lastName,
                params.getProperty("phoneNumber", ""), address);
        return office.register(customer);
    }

    private void checkParameters(Properties params) {
        require(params, "name");
        require(params, "address");
    }

    private void require(Properties params, String name) {
        if (params == null || params.getProperty(name) == null
                || params.getProperty(name).isBlank()) {
            throw new IllegalArgumentException("Missing parameter: " + name);
        }
    }
}