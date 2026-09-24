package com.example.parkingsystem;

import com.example.parkingsystem.charges.strategy.ParkingChargeStrategy;

import java.time.LocalDateTime;
import java.util.Objects;

public class ParkingLot {
    private final String name;
    private final Money baseRate;
    private ParkingChargeStrategy chargeStrategy;

    public ParkingLot(String name, Money baseRate,
                      ParkingChargeStrategy chargeStrategy) {
        this.name = Objects.requireNonNull(name, "name");
        this.baseRate = Objects.requireNonNull(baseRate, "baseRate");
        this.chargeStrategy = Objects.requireNonNull(chargeStrategy,
                "chargeStrategy");
    }

    public String getName() {
        return name;
    }

    public Money getBaseRate() {
        return baseRate;
    }

    public ParkingChargeStrategy getChargeStrategy() {
        return chargeStrategy;
    }

    public void setChargeStrategy(ParkingChargeStrategy chargeStrategy) {
        this.chargeStrategy = Objects.requireNonNull(chargeStrategy,
                "chargeStrategy");
    }

    public Money calculateCharge(LocalDateTime entryTime, LocalDateTime exitTime,
                                 ParkingPermit permit) {
        return chargeStrategy.calculateCharge(baseRate, entryTime, exitTime,
                permit);
    }
}