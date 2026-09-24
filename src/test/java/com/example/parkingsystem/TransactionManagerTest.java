package com.example.parkingsystem;

import com.example.parkingsystem.charges.strategy.ParkingChargeStrategyFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class TransactionManagerTest {
    @Test
    void createsTransactionUsingLotStrategy() {
        ParkingLot lot = new ParkingLot("Main Lot", new Money(2),
                new ParkingChargeStrategyFactory().create(true));
        ParkingPermit permit = new ParkingPermit("permit-1",
                new Car(CarType.SUV, "ABC-123", null));
        LocalDateTime entry = LocalDateTime.of(2026, 9, 24, 9, 0);
        LocalDateTime exit = LocalDateTime.of(2026, 9, 24, 10, 1);

        ParkingTransaction transaction = new TransactionManager()
                .createTransaction(lot, permit, entry, exit);

        assertSame(lot, transaction.getParkingLot());
        assertSame(permit, transaction.getPermit());
        assertEquals(entry, transaction.getEntryTime());
        assertEquals(exit, transaction.getExitTime());
        assertEquals(new Money(3.20), transaction.getCharge());
    }
}