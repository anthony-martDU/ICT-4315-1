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

        Money charge = parkingLot.calculateCharge(entryTime, exitTime, permit);
        ParkingTransaction transaction = new ParkingTransaction(parkingLot,
                permit, entryTime, exitTime, charge);
        recordTransaction(transaction);
        return transaction;
    }

    public void recordTransaction(ParkingTransaction transaction) {
        transactions.add(Objects.requireNonNull(transaction, "transaction"));
    }

    public List<ParkingTransaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }
}