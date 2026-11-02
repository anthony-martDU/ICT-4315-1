package com.example.parkingsystem;

import com.example.parkingsystem.protocol.ParkingRequest;
import com.example.parkingsystem.protocol.ParkingRequestProcessor;
import com.example.parkingsystem.protocol.ParkingResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class ServerClientTest {
    @Test
    void clientExchangesCustomerAndCarRequestsWithServer() throws Exception {
        ParkingRequestProcessor processor = new ParkingRequestProcessor(
                new ParkingService(new ParkingOffice()));
        AtomicReference<Throwable> serverFailure = new AtomicReference<>();

        try (ServerSocket serverSocket = new ServerSocket(0)) {
            Thread serverThread = new Thread(() -> {
                try {
                    for (int requestNumber = 0; requestNumber < 2; requestNumber++) {
                        try (Socket socket = serverSocket.accept()) {
                            Server.handleConnection(socket, processor);
                        }
                    }
                } catch (Throwable failure) {
                    serverFailure.set(failure);
                }
            });
            serverThread.start();

            Properties customerParameters = new Properties();
            customerParameters.setProperty("name", "Rob Smith");
            customerParameters.setProperty("address", "123 Main St");
            ParkingResponse customerResponse = ServerClient.send("127.0.0.1",
                    serverSocket.getLocalPort(),
                    new ParkingRequest("CUSTOMER", customerParameters));

            Properties carParameters = new Properties();
            carParameters.setProperty("licensePlate", "ROB4CO");
            carParameters.setProperty("type", "SUV");
            carParameters.setProperty("customerId", customerResponse.getMessage());
            ParkingResponse carResponse = ServerClient.send("127.0.0.1",
                    serverSocket.getLocalPort(),
                    new ParkingRequest("CAR", carParameters));

            serverThread.join(2000);

            assertFalse(serverThread.isAlive());
            assertNull(serverFailure.get());
            assertEquals(200, customerResponse.getStatusCode());
            assertEquals("customer-1", customerResponse.getMessage());
            assertEquals(200, carResponse.getStatusCode());
            assertEquals("permit-1", carResponse.getMessage());
        }
    }
}