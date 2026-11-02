package com.example.parkingsystem;

import com.example.parkingsystem.protocol.ParkingRequest;
import com.example.parkingsystem.protocol.ParkingResponse;
import com.example.parkingsystem.protocol.JsonMessageIO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Locale;
import java.util.Properties;

public class ServerClient {
    private static final String DEFAULT_HOST = "127.0.0.1";

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            printUsage();
            return;
        }

        String host = System.getenv().getOrDefault(
                "PARKING_SERVER_HOST", DEFAULT_HOST);
        int port = Integer.parseInt(System.getenv().getOrDefault(
                "PARKING_SERVER_PORT", String.valueOf(Server.DEFAULT_PORT)));
        String commandName = args[0].toUpperCase(Locale.ROOT);
        Properties parameters = parseParameters(args);
        ParkingRequest request = new ParkingRequest(commandName, parameters);
        ParkingResponse response = send(host, port, request);

        if (response.getStatusCode() >= 400) {
            System.err.println(response);
            System.exit(1);
        }
        System.out.println(response);
    }

    static ParkingResponse send(String host, int port, ParkingRequest request)
            throws IOException {
        try (Socket socket = new Socket(host, port)) {
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());
            JsonMessageIO.write(output, request.toJson());
            return ParkingResponse.fromJson(JsonMessageIO.read(input));
        }
    }

    private static Properties parseParameters(String[] args) {
        Properties parameters = new Properties();
        for (int index = 1; index < args.length; index++) {
            String[] parts = args[index].split("=", 2);
            if (parts.length != 2 || parts[0].isBlank()) {
                throw new IllegalArgumentException(
                        "Expected parameter in key=value form: " + args[index]);
            }
            parameters.setProperty(canonicalParameterName(parts[0]), parts[1]);
        }
        return parameters;
    }

    private static String canonicalParameterName(String name) {
        return switch (name.toLowerCase(Locale.ROOT)) {
            case "name", "firstname" -> "name";
            case "address" -> "address";
            case "phonenumber" -> "phoneNumber";
            case "license", "licenseplate" -> "licensePlate";
            case "type" -> "type";
            case "customer", "customerid" -> "customerId";
            default -> name;
        };
    }

    private static void printUsage() {
        System.err.println("Usage: ServerClient CUSTOMER name=\"Rob Smith\" "
                + "address=\"123 Main St\" [phoneNumber=555-0100]");
        System.err.println("   or: ServerClient CAR licensePlate=ROB4CO "
                + "type=SUV customerId=customer-1");
        System.err.println("Set PARKING_SERVER_HOST and PARKING_SERVER_PORT "
                + "to override the connection defaults.");
    }
}