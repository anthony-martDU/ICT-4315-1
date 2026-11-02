package com.example.parkingsystem.protocol;

import com.google.gson.Gson;

import java.util.Objects;

public class ParkingResponse {
    private static final Gson GSON = new Gson();

    private int statusCode;
    private String message;

    public ParkingResponse() {
        this(0, "");
    }

    public ParkingResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = Objects.requireNonNull(message, "message");
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public String toJson() {
        return GSON.toJson(this);
    }

    public static ParkingResponse fromJson(String json) {
        ParkingResponse response = GSON.fromJson(json, ParkingResponse.class);
        if (response == null) {
            throw new IllegalArgumentException("Response JSON cannot be null");
        }
        if (response.message == null) {
            throw new IllegalArgumentException("Response message is required");
        }
        return response;
    }

    @Override
    public String toString() {
        return "ParkingResponse{statusCode=" + statusCode
                + ", message='" + message + "'}";
    }
}