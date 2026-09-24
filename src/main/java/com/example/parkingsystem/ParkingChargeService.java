package com.example.parkingsystem;

import java.time.LocalDateTime;
import java.util.Objects;

public class ParkingChargeService {
    public Money calculateCharge(ParkingLot parkingLot, ParkingPermit permit,
                                 LocalDateTime entryTime,
                                 LocalDateTime exitTime) {
        Objects.requireNonNull(parkingLot, "parkingLot");
        Objects.requireNonNull(permit, "permit");
        return parkingLot.calculateCharge(entryTime, exitTime, permit);
    }
}