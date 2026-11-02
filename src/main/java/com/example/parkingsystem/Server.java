package com.example.parkingsystem;

import com.example.parkingsystem.protocol.ParkingRequestProcessor;
import com.example.parkingsystem.protocol.JsonMessageIO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int DEFAULT_PORT = 5050;

    public static void main(String[] args) throws IOException {
        int port = args.length == 0 ? DEFAULT_PORT : Integer.parseInt(args[0]);
        ParkingRequestProcessor processor = new ParkingRequestProcessor(
                new ParkingService(new ParkingOffice()));

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Parking server listening on port " + port);
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    handleConnection(clientSocket, processor);
                } catch (IOException exception) {
                    System.err.println("Client connection failed: "
                            + exception.getMessage());
                }
            }
        }
    }

    static void handleConnection(Socket socket,
                                 ParkingRequestProcessor processor)
            throws IOException {
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        String requestJson = JsonMessageIO.read(input);
        String responseJson = processor.process(requestJson).toJson();
        JsonMessageIO.write(output, responseJson);
    }
}