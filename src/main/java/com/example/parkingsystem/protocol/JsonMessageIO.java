package com.example.parkingsystem.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class JsonMessageIO {
    private static final int MAX_MESSAGE_BYTES = 1_048_576;

    private JsonMessageIO() {
    }

    public static String read(DataInputStream input) throws IOException {
        int length = input.readInt();
        if (length < 0 || length > MAX_MESSAGE_BYTES) {
            throw new IOException("Invalid JSON message length: " + length);
        }
        byte[] bytes = new byte[length];
        input.readFully(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static void write(DataOutputStream output, String message)
            throws IOException {
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        if (bytes.length > MAX_MESSAGE_BYTES) {
            throw new IOException("JSON message exceeds maximum size");
        }
        output.writeInt(bytes.length);
        output.write(bytes);
        output.flush();
    }
}