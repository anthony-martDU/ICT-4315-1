package com.example.parkingsystem;

import java.util.Objects;
import java.util.HashMap;
import java.util.Map;

public class ParkingObserver implements ParkingAction {
    private final TransactionManager transactionManager;
    private final Map<VisitKey, ParkingEvent> activeVisits = new HashMap<>();

    public ParkingObserver(TransactionManager transactionManager,
                           ParkingOffice parkingOffice) {
        this.transactionManager = Objects.requireNonNull(
                transactionManager, "transactionManager");
        Objects.requireNonNull(parkingOffice, "parkingOffice")
                .getParkingLots()
                .forEach(parkingLot -> parkingLot.addObserver(this));
    }

    @Override
    public void update(ParkingEvent event) {
        Objects.requireNonNull(event, "event");
        VisitKey key = new VisitKey(event.getParkingLot(), event.getPermit());

        if (event.getType() == ParkingEvent.Type.ENTRY) {
            if (activeVisits.putIfAbsent(key, event) != null) {
                throw new IllegalStateException(
                        "Permit already has an active visit in this parking lot");
            }
            return;
        }

        ParkingEvent entryEvent = activeVisits.get(key);
        if (entryEvent == null) {
            throw new IllegalStateException(
                    "Cannot exit without a matching parking entry");
        }

        Money charge = event.getParkingLot().calculateCharge(
                entryEvent.getTimestamp(), event.getTimestamp(), event.getPermit());
        ParkingTransaction transaction = new ParkingTransaction(
                event.getParkingLot(), event.getPermit(),
                entryEvent.getTimestamp(), event.getTimestamp(), charge);
        transactionManager.recordTransaction(transaction);
        activeVisits.remove(key);
    }

    private record VisitKey(ParkingLot parkingLot, ParkingPermit permit) {
    }
}