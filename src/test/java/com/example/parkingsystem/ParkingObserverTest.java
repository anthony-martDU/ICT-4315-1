package com.example.parkingsystem;

import edu.du.ict4315.charges.decorator.DefaultParkingChargeCalculatorFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParkingObserverTest {
    private final LocalDateTime entryTime = LocalDateTime.of(2026, 9, 24, 9, 0);
    private final LocalDateTime exitTime = LocalDateTime.of(2026, 9, 24, 10, 0);

    @Test
    void singleObserverCreatesTransactionAfterExit() {
        ParkingLot lot = createLot();
        ParkingPermit permit = createPermit();
        TransactionManager manager = new TransactionManager();
        ParkingOffice office = createOfficeWith(lot);
        new ParkingObserver(manager, office);

        lot.enter(permit, entryTime);
        assertEquals(0, manager.getTransactions().size());

        lot.exit(permit, exitTime);

        assertEquals(1, manager.getTransactions().size());
        ParkingTransaction transaction = manager.getTransactions().get(0);
        assertEquals(lot, transaction.getParkingLot());
        assertEquals(permit, transaction.getPermit());
        assertEquals(entryTime, transaction.getEntryTime());
        assertEquals(exitTime, transaction.getExitTime());
        assertEquals(new Money(2), transaction.getCharge());
    }

    @Test
    void notifiesMultipleObservers() {
        ParkingLot lot = createLot();
        ParkingPermit permit = createPermit();
        TransactionManager firstManager = new TransactionManager();
        TransactionManager secondManager = new TransactionManager();
        ParkingOffice office = createOfficeWith(lot);
        new ParkingObserver(firstManager, office);
        new ParkingObserver(secondManager, office);

        lot.enter(permit, entryTime);
        lot.exit(permit, exitTime);

        assertEquals(1, firstManager.getTransactions().size());
        assertEquals(1, secondManager.getTransactions().size());
    }

    @Test
    void removedObserverDoesNotReceiveEventsAndDuplicateIsIgnored() {
        ParkingLot lot = createLot();
        ParkingPermit permit = createPermit();
        TransactionManager manager = new TransactionManager();
        ParkingOffice office = createOfficeWith(lot);
        ParkingObserver observer = new ParkingObserver(manager, office);
        lot.addObserver(observer);
        lot.removeObserver(observer);

        lot.enter(permit, entryTime);
        lot.exit(permit, exitTime);

        assertEquals(0, manager.getTransactions().size());
    }

    @Test
    void exitWithoutObservedEntryIsRejected() {
        ParkingLot lot = createLot();
        ParkingPermit permit = createPermit();
        ParkingOffice office = createOfficeWith(lot);
        new ParkingObserver(new TransactionManager(), office);

        assertThrows(IllegalStateException.class,
                () -> lot.exit(permit, exitTime));
    }

    private ParkingLot createLot() {
        return new ParkingLot("Main Lot", new Money(2),
            new DefaultParkingChargeCalculatorFactory(false));
    }

    private ParkingPermit createPermit() {
        return new ParkingPermit("permit-1",
                new Car(CarType.SUV, "ABC-123", null));
    }

    private ParkingOffice createOfficeWith(ParkingLot lot) {
        ParkingOffice office = new ParkingOffice();
        office.addParkingLot(lot);
        return office;
    }
}