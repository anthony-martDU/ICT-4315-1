package com.example.parkingsystem;

import com.example.parkingsystem.protocol.ParkingRequest;
import com.example.parkingsystem.protocol.ParkingRequestProcessor;
import com.example.parkingsystem.protocol.ParkingResponse;
import org.junit.jupiter.api.Test;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class ServerConcurrencyTest {
    private static final int CLIENT_COUNT = 8;

    @Test
    void handlesEightSimultaneousCustomerRequestsWithUniqueIds() throws Exception {
        ParkingOffice office = new ParkingOffice();
        CountDownLatch requestsEnteredProcessor = new CountDownLatch(CLIENT_COUNT);
        ParkingRequestProcessor processor = new ParkingRequestProcessor(
                new ParkingService(office)) {
            @Override
            public ParkingResponse process(String json) {
                requestsEnteredProcessor.countDown();
                try {
                    if (!requestsEnteredProcessor.await(3, TimeUnit.SECONDS)) {
                        return new ParkingResponse(503,
                                "Requests were not processed concurrently");
                    }
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    return new ParkingResponse(503, "Request was interrupted");
                }
                return super.process(json);
            }
        };
        ThreadPoolExecutor serverExecutor = Server.createClientExecutor();
        ExecutorService clients = Executors.newFixedThreadPool(CLIENT_COUNT);
        AtomicReference<Throwable> serverFailure = new AtomicReference<>();
        Thread serverThread;

        try (ServerSocket serverSocket = new ServerSocket(0)) {
            serverThread = new Thread(() -> {
                try {
                    Server.serve(serverSocket, serverExecutor, processor);
                } catch (Throwable failure) {
                    serverFailure.set(failure);
                }
            });
            serverThread.start();

            CountDownLatch startClients = new CountDownLatch(1);
            List<Future<ParkingResponse>> responses = new ArrayList<>();
            for (int index = 0; index < CLIENT_COUNT; index++) {
                int customerIndex = index;
                responses.add(clients.submit(() -> {
                    startClients.await();
                    Properties parameters = new Properties();
                    parameters.setProperty("name", "Customer " + customerIndex);
                    parameters.setProperty("address", "Address " + customerIndex);
                    return ServerClient.send("127.0.0.1",
                            serverSocket.getLocalPort(),
                            new ParkingRequest("CUSTOMER", parameters));
                }));
            }
            startClients.countDown();

            Set<String> customerIds = new HashSet<>();
            Set<String> expectedCustomerIds = new HashSet<>();
            for (int index = 1; index <= CLIENT_COUNT; index++) {
                expectedCustomerIds.add("customer-" + index);
            }
            for (Future<ParkingResponse> responseFuture : responses) {
                ParkingResponse response = responseFuture.get(5, TimeUnit.SECONDS);
                assertEquals(200, response.getStatusCode(), response.getMessage());
                customerIds.add(response.getMessage());
            }

            assertEquals(expectedCustomerIds, customerIds);
            assertNull(serverFailure.get());
            serverSocket.close();
            serverThread.join(2000);
            assertFalse(serverThread.isAlive());
        } finally {
            clients.shutdownNow();
            serverExecutor.shutdownNow();
        }
    }
}