package com.example.parkingsystem;

import edu.du.ict4315.charges.decorator.DefaultParkingChargeCalculatorFactory;
import edu.du.ict4315.charges.decorator.ParkingChargeCalculator;
import edu.du.ict4315.charges.decorator.ParkingChargeCalculatorFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ParkingLotTest {
    @Test
    void lotUsesItsConfiguredStrategy() {
        ParkingChargeCalculatorFactory factory =
                new DefaultParkingChargeCalculatorFactory(false);
        ParkingLot lot = new ParkingLot("Main Lot", new Money(15), factory);
        ParkingPermit permit = new ParkingPermit("permit-1",
                new Car(CarType.COMPACT, "ABC-123", null));

        assertSame(factory, lot.getChargeCalculatorFactory());
        assertEquals(new Money(12), lot.calculateCharge(
                LocalDateTime.of(2026, 9, 24, 9, 0),
                LocalDateTime.of(2026, 9, 24, 10, 0), permit));
    }

    @Test
    void lotCanReplaceItsStrategy() {
        ParkingLot lot = new ParkingLot("Main Lot", new Money(15),
                new DefaultParkingChargeCalculatorFactory(false));
        ParkingChargeCalculator replacementCalculator =
                new ParkingChargeCalculator() {
                    @Override
                    public Money getParkingCharge(LocalDateTime entryTime,
                            LocalDateTime exitTime, ParkingLot parkingLot,
                            ParkingPermit permit) {
                        return new Money(7);
                    }
                };
        ParkingChargeCalculatorFactory replacementFactory =
                () -> replacementCalculator;

        lot.setChargeCalculatorFactory(replacementFactory);

        assertSame(replacementFactory, lot.getChargeCalculatorFactory());
        assertEquals(new Money(7), lot.calculateCharge(
                LocalDateTime.of(2026, 9, 24, 9, 0),
                LocalDateTime.of(2026, 9, 24, 10, 0), null));
    }
}