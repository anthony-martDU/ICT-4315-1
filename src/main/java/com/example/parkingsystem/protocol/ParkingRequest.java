package com.example.parkingsystem.protocol;

import com.google.gson.Gson;

import java.util.Objects;
import java.util.Properties;

public class ParkingRequest {
    private static final Gson GSON = new Gson();

    private String commandName;
    private Properties parameters;

    public ParkingRequest() {
        this("", new Properties());
    }

    public ParkingRequest(String commandName, Properties parameters) {
        this.commandName = Objects.requireNonNull(commandName, "commandName");
        this.parameters = new Properties();
        if (parameters != null) {
            this.parameters.putAll(parameters);
        }
    }

    public String getCommandName() {
        return commandName;
    }

    public Properties getParameters() {
        Properties copy = new Properties();
        if (parameters != null) {
            copy.putAll(parameters);
        }
        return copy;
    }

    public String toJson() {
        return GSON.toJson(this);
    }

    public static ParkingRequest fromJson(String json) {
        ParkingRequest request = GSON.fromJson(json, ParkingRequest.class);
        if (request == null) {
            throw new IllegalArgumentException("Request JSON cannot be null");
        }
        if (request.commandName == null || request.commandName.isBlank()) {
            throw new IllegalArgumentException("Request commandName is required");
        }
        if (request.parameters == null) {
            request.parameters = new Properties();
        }
        return request;
    }

    @Override
    public String toString() {
        return "ParkingRequest{commandName='" + commandName
                + "', parameters=" + parameters + "}";
    }
}