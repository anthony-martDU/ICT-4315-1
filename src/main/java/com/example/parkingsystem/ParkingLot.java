package com.example.parkingsystem;

import com.example.parkingsystem.charges.factory.ParkingChargeStrategyFactory;
import com.example.parkingsystem.charges.strategy.ParkingChargeStrategy;

import java.time.LocalDateTime;
import java.util.Objects;

public class ParkingLot {
    private final String name;
    private final Money baseRate;
        private ParkingChargeStrategyFactory chargeStrategyFactory;

    public ParkingLot(String name, Money baseRate,
                  ParkingChargeStrategyFactory chargeStrategyFactory) {
        this.name = Objects.requireNonNull(name, "name");
        this.baseRate = Objects.requireNonNull(baseRate, "baseRate");
        this.chargeStrategyFactory = Objects.requireNonNull(
            chargeStrategyFactory, "chargeStrategyFactory");
    }

    public String getName() {
        return name;
    }

    public Money getBaseRate() {
        return baseRate;
    }

    public ParkingChargeStrategyFactory getChargeStrategyFactory() {
        return chargeStrategyFactory;
    }

    public void setChargeStrategyFactory(
            ParkingChargeStrategyFactory chargeStrategyFactory) {
        this.chargeStrategyFactory = Objects.requireNonNull(
                chargeStrategyFactory, "chargeStrategyFactory");
    }

    public Money calculateCharge(LocalDateTime entryTime, LocalDateTime exitTime,
                                 ParkingPermit permit) {
        ParkingChargeStrategy strategy = chargeStrategyFactory.createStrategy();
        return strategy.calculateCharge(baseRate, entryTime, exitTime, permit);
    }
}