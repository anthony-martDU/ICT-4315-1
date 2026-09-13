package com.example.parkingsystem;

import java.util.Properties;

public class RegisterCarCommand implements Command {
    private final ParkingOffice office;

    public RegisterCarCommand(ParkingOffice office) {
        this.office = office;
    }

    @Override
    public String getCommandName() {
        return "CAR";
    }

    @Override
    public String getDisplayName() {
        return "Register Car";
    }

    @Override
    public String execute(Properties params) {
        checkParameters(params);

        Customer owner = office.getCustomer(params.getProperty("customerId"));
        if (owner == null) {
            throw new IllegalArgumentException("Unknown customer: "
                    + params.getProperty("customerId"));
        }

        CarType type;
        try {
            type = CarType.valueOf(params.getProperty("type").toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Unknown car type: "
                    + params.getProperty("type"), exception);
        }

        Car car = new Car(type, params.getProperty("licensePlate"), owner);
        return office.register(car);
    }

    private void checkParameters(Properties params) {
        require(params, "licensePlate");
        require(params, "type");
        require(params, "customerId");
    }

    private void require(Properties params, String name) {
        if (params == null || params.getProperty(name) == null
                || params.getProperty(name).isBlank()) {
            throw new IllegalArgumentException("Missing parameter: " + name);
        }
    }
}