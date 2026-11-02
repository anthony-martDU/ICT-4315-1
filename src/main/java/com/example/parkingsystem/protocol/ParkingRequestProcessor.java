package com.example.parkingsystem.protocol;

import com.example.parkingsystem.ParkingService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class ParkingRequestProcessor {
    private final ParkingService parkingService;

    public ParkingRequestProcessor(ParkingService parkingService) {
        this.parkingService = Objects.requireNonNull(parkingService,
                "parkingService");
    }

    public ParkingResponse process(String json) {
        ParkingRequest request;
        try {
            request = ParkingRequest.fromJson(json);
        } catch (RuntimeException exception) {
            return new ParkingResponse(400, "Invalid request JSON");
        }

        try {
            Properties parameters = request.getParameters();
            List<String> parameterArguments = new ArrayList<>();
            for (String name : parameters.stringPropertyNames()) {
                parameterArguments.add(name + "=" + parameters.getProperty(name));
            }
            String result = parkingService.performCommand(
                    request.getCommandName(),
                    parameterArguments.toArray(String[]::new));
            return new ParkingResponse(200, result);
        } catch (RuntimeException exception) {
            String message = exception.getMessage();
            return new ParkingResponse(400,
                    message == null ? "Invalid request" : message);
        }
    }
}