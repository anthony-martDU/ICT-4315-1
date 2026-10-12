package com.example.parkingsystem;

import java.time.LocalDateTime;
import java.util.Objects;

public final class ParkingEvent {
    public enum Type {
        ENTRY,
        EXIT
    }

    private final Type type;
    private final ParkingLot parkingLot;
    private final ParkingPermit permit;
    private final LocalDateTime timestamp;

    public ParkingEvent(Type type, ParkingLot parkingLot, ParkingPermit permit,
                        LocalDateTime timestamp) {
        this.type = Objects.requireNonNull(type, "type");
        this.parkingLot = Objects.requireNonNull(parkingLot, "parkingLot");
        this.permit = Objects.requireNonNull(permit, "permit");
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp");
    }

    public Type getType() {
        return type;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public ParkingPermit getPermit() {
        return permit;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}