package com.example.parkingsystem;

import com.example.parkingsystem.charges.factory.ParkingChargeStrategyFactory;
import com.example.parkingsystem.charges.strategy.ParkingChargeStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParkingLot {
    private final String name;
    private final Money baseRate;
    private ParkingChargeStrategyFactory chargeStrategyFactory;
    private final List<ParkingAction> observers = new ArrayList<>();

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

    public void addObserver(ParkingAction observer) {
        Objects.requireNonNull(observer, "observer");
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(ParkingAction observer) {
        observers.remove(Objects.requireNonNull(observer, "observer"));
    }

    public void enter(ParkingPermit permit) {
        enter(permit, LocalDateTime.now());
    }

    public void enter(ParkingPermit permit, LocalDateTime timestamp) {
        notifyObservers(new ParkingEvent(ParkingEvent.Type.ENTRY, this, permit,
                timestamp));
    }

    public void exit(ParkingPermit permit) {
        exit(permit, LocalDateTime.now());
    }

    public void exit(ParkingPermit permit, LocalDateTime timestamp) {
        notifyObservers(new ParkingEvent(ParkingEvent.Type.EXIT, this, permit,
                timestamp));
    }

    private void notifyObservers(ParkingEvent event) {
        for (ParkingAction observer : List.copyOf(observers)) {
            observer.update(event);
        }
    }
}