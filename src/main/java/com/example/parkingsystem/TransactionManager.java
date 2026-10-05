package com.example.parkingsystem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TransactionManager {
    private final List<ParkingTransaction> transactions = new ArrayList<>();

    public ParkingTransaction park(ParkingLot parkingLot,
                  ParkingPermit permit,
                  LocalDateTime entryTime,
                  LocalDateTime exitTime) {
        Objects.requireNonNull(parkingLot, "parkingLot");
        Objects.requireNonNull(permit, "permit");
        Objects.requireNonNull(entryTime, "entryTime");
        Objects.requireNonNull(exitTime, "exitTime");

    Money charge = parkingLot.getChargeStrategyFactory().createStrategy()
        .calculateCharge(parkingLot.getBaseRate(), entryTime,
            exitTime, permit);
    ParkingTransaction transaction = new ParkingTransaction(parkingLot,
        permit, entryTime, exitTime, charge);
    transactions.add(transaction);
    return transaction;
    }

    public List<ParkingTransaction> getTransactions() {
    return Collections.unmodifiableList(transactions);
    }
}