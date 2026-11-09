package com.example.parkingsystem;

import com.example.parkingsystem.protocol.ParkingRequestProcessor;
import com.example.parkingsystem.protocol.JsonMessageIO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {
    public static final int DEFAULT_PORT = 5050;
    private static final int WORKER_COUNT = 8;
    private static final int MAX_QUEUED_CLIENTS = 100;
    private static final int CLIENT_READ_TIMEOUT_MILLIS = 10_000;

    public static void main(String[] args) throws IOException {
        int port = args.length == 0 ? DEFAULT_PORT : Integer.parseInt(args[0]);
        ParkingRequestProcessor processor = new ParkingRequestProcessor(
                new ParkingService(new ParkingOffice()));
        ThreadPoolExecutor clientExecutor = createClientExecutor();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Parking server listening on port " + port);
            serve(serverSocket, clientExecutor, processor);
        } finally {
            shutdown(clientExecutor);
        }
    }

    static ThreadPoolExecutor createClientExecutor() {
        return new ThreadPoolExecutor(WORKER_COUNT, WORKER_COUNT,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(MAX_QUEUED_CLIENTS),
                new ThreadPoolExecutor.AbortPolicy());
    }

    static void serve(ServerSocket serverSocket,
                      ThreadPoolExecutor clientExecutor,
                      ParkingRequestProcessor processor) throws IOException {
        while (!serverSocket.isClosed()) {
            Socket clientSocket;
            try {
                clientSocket = serverSocket.accept();
            } catch (SocketException exception) {
                if (serverSocket.isClosed()) {
                    return;
                }
                throw exception;
            }

            try {
                clientSocket.setSoTimeout(CLIENT_READ_TIMEOUT_MILLIS);
                clientExecutor.execute(() -> handleClient(clientSocket, processor));
            } catch (RejectedExecutionException exception) {
                closeRejectedClient(clientSocket);
            } catch (IOException exception) {
                closeRejectedClient(clientSocket);
                System.err.println("Could not configure client connection: "
                        + exception.getMessage());
            }
        }
    }

    private static void handleClient(Socket clientSocket,
                                     ParkingRequestProcessor processor) {
        try (Socket socket = clientSocket) {
            handleConnection(socket, processor);
        } catch (IOException exception) {
            System.err.println("Client connection failed: "
                    + exception.getMessage());
        }
    }

    private static void closeRejectedClient(Socket clientSocket) {
        try {
            clientSocket.close();
        } catch (IOException exception) {
            System.err.println("Could not close rejected client: "
                    + exception.getMessage());
        }
    }

    private static void shutdown(ThreadPoolExecutor clientExecutor) {
        clientExecutor.shutdown();
        try {
            if (!clientExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                clientExecutor.shutdownNow();
            }
        } catch (InterruptedException exception) {
            clientExecutor.shutdownNow();
            Thread.currentThread().interrupt();
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