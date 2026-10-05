package com.example.parkingsystem;

import com.example.parkingsystem.charges.factory.DefaultParkingChargeStrategyFactory;
import com.example.parkingsystem.charges.factory.ParkingChargeStrategyFactory;
import com.example.parkingsystem.charges.strategy.ParkingChargeStrategy;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class TransactionManagerTest {
    @Test
    void createsTransactionUsingLotStrategy() {
        ParkingLot lot = new ParkingLot("Main Lot", new Money(2),
                new DefaultParkingChargeStrategyFactory(true));
        ParkingPermit permit = new ParkingPermit("permit-1",
                new Car(CarType.COMPACT, "ABC-123", null));
        LocalDateTime entry = LocalDateTime.of(2026, 9, 24, 9, 0);
        LocalDateTime exit = LocalDateTime.of(2026, 9, 24, 10, 1);

        ParkingTransaction transaction = new TransactionManager()
                .park(lot, permit, entry, exit);

        assertSame(lot, transaction.getParkingLot());
        assertSame(permit, transaction.getPermit());
        assertEquals(entry, transaction.getEntryTime());
        assertEquals(exit, transaction.getExitTime());
        assertEquals(new Money(3.20), transaction.getCharge());
    }

        @Test
        void parkRecordsCreatedTransaction() {
                ParkingLot lot = new ParkingLot("Main Lot", new Money(2),
                                new DefaultParkingChargeStrategyFactory(false));
                ParkingPermit permit = new ParkingPermit("permit-1",
                                new Car(CarType.SUV, "ABC-123", null));
                TransactionManager manager = new TransactionManager();

                ParkingTransaction transaction = manager.park(lot, permit,
                                LocalDateTime.of(2026, 9, 24, 9, 0),
                                LocalDateTime.of(2026, 9, 24, 10, 0));

                assertSame(transaction, manager.getTransactions().get(0));
        }

        @Test
        void parkRequestsStrategyFromLotFactory() {
                AtomicInteger factoryCalls = new AtomicInteger();
                ParkingChargeStrategy strategy =
                                (baseRate, entryTime, exitTime, permit) -> new Money(9);
                ParkingChargeStrategyFactory factory = () -> {
                        factoryCalls.incrementAndGet();
                        return strategy;
                };
                ParkingLot lot = new ParkingLot("Main Lot", new Money(2), factory);
                ParkingPermit permit = new ParkingPermit("permit-1",
                                new Car(CarType.SUV, "ABC-123", null));

                ParkingTransaction transaction = new TransactionManager().park(lot,
                                permit, LocalDateTime.of(2026, 9, 24, 9, 0),
                                LocalDateTime.of(2026, 9, 24, 10, 0));

                assertEquals(1, factoryCalls.get());
                assertEquals(new Money(9), transaction.getCharge());
        }
}