package com.example.parkingsystem;

import java.time.LocalDateTime;
import java.util.Objects;

public final class ParkingTransaction {
    private final ParkingLot parkingLot;
    private final ParkingPermit permit;
    private final LocalDateTime entryTime;
    private final LocalDateTime exitTime;
    private final Money charge;

    public ParkingTransaction(ParkingLot parkingLot, ParkingPermit permit,
                              LocalDateTime entryTime, LocalDateTime exitTime,
                              Money charge) {
        this.parkingLot = Objects.requireNonNull(parkingLot, "parkingLot");
        this.permit = Objects.requireNonNull(permit, "permit");
        this.entryTime = Objects.requireNonNull(entryTime, "entryTime");
        this.exitTime = Objects.requireNonNull(exitTime, "exitTime");
        this.charge = Objects.requireNonNull(charge, "charge");
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public ParkingPermit getPermit() {
        return permit;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public Money getCharge() {
        return charge;
    }
}