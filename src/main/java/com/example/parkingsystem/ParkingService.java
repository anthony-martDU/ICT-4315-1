package com.example.parkingsystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ParkingService {
    private final ParkingOffice office;
    private final Map<String, Command> commands = new HashMap<>();

    public ParkingService(ParkingOffice office) {
        this.office = office;
        register(new RegisterCustomerCommand(office));
        register(new RegisterCarCommand(office));
    }

    private void register(Command command) {
        commands.put(command.getCommandName(), command);
    }

    public String performCommand(String commandName, String[] parameters) {
        Command command = commands.get(commandName);
        if (command == null) {
            throw new IllegalArgumentException("Unknown command: " + commandName);
        }

        Properties properties = new Properties();
        if (parameters != null) {
            for (String parameter : parameters) {
                String[] parts = parameter.split("=", 2);
                if (parts.length != 2 || parts[0].isBlank()) {
                    throw new IllegalArgumentException(
                            "Invalid parameter: " + parameter);
                }
                properties.setProperty(parts[0], parts[1]);
            }
        }
        return command.execute(properties);
    }
}