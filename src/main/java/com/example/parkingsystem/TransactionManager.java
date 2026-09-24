package com.example.parkingsystem;

import java.time.LocalDateTime;
import java.util.Objects;

public class TransactionManager {
    public ParkingTransaction createTransaction(ParkingLot parkingLot,
                                                 ParkingPermit permit,
                                                 LocalDateTime entryTime,
                                                 LocalDateTime exitTime) {
        Objects.requireNonNull(parkingLot, "parkingLot");
        Objects.requireNonNull(permit, "permit");
        Objects.requireNonNull(entryTime, "entryTime");
        Objects.requireNonNull(exitTime, "exitTime");

        Money charge = parkingLot.calculateCharge(entryTime, exitTime, permit);
        return new ParkingTransaction(parkingLot, permit, entryTime, exitTime,
                charge);
    }
}