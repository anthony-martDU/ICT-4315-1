package com.example.parkingsystem;

import edu.du.ict4315.charges.decorator.ParkingChargeCalculator;
import edu.du.ict4315.charges.decorator.ParkingChargeCalculatorFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParkingLot {
    private final String name;
    private final Money baseRate;
    private ParkingChargeCalculatorFactory chargeCalculatorFactory;
    private final List<ParkingAction> observers = new ArrayList<>();

    public ParkingLot(String name, Money baseRate,
                  ParkingChargeCalculatorFactory chargeCalculatorFactory) {
        this.name = Objects.requireNonNull(name, "name");
        this.baseRate = Objects.requireNonNull(baseRate, "baseRate");
        this.chargeCalculatorFactory = Objects.requireNonNull(
                chargeCalculatorFactory, "chargeCalculatorFactory");
    }

    public String getName() {
        return name;
    }

    public Money getBaseRate() {
        return baseRate;
    }

    public ParkingChargeCalculatorFactory getChargeCalculatorFactory() {
        return chargeCalculatorFactory;
    }

    public void setChargeCalculatorFactory(
            ParkingChargeCalculatorFactory chargeCalculatorFactory) {
        this.chargeCalculatorFactory = Objects.requireNonNull(
                chargeCalculatorFactory, "chargeCalculatorFactory");
    }

    public Money calculateCharge(LocalDateTime entryTime, LocalDateTime exitTime,
                                 ParkingPermit permit) {
        ParkingChargeCalculator calculator =
            chargeCalculatorFactory.createCalculator();
        return calculator.getParkingCharge(entryTime, exitTime, this, permit);
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