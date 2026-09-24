package com.example.parkingsystem;

import com.example.parkingsystem.charges.strategy.DailyParkingChargeStrategy;
import com.example.parkingsystem.charges.strategy.ParkingChargeStrategy;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ParkingLotTest {
    @Test
    void lotUsesItsConfiguredStrategy() {
        ParkingChargeStrategy strategy = new DailyParkingChargeStrategy();
        ParkingLot lot = new ParkingLot("Main Lot", new Money(15), strategy);
        ParkingPermit permit = new ParkingPermit("permit-1",
                new Car(CarType.COMPACT, "ABC-123", null));

        assertSame(strategy, lot.getChargeStrategy());
        assertEquals(new Money(12), lot.calculateCharge(
                LocalDateTime.of(2026, 9, 24, 9, 0),
                LocalDateTime.of(2026, 9, 24, 10, 0), permit));
    }

    @Test
    void lotCanReplaceItsStrategy() {
        ParkingLot lot = new ParkingLot("Main Lot", new Money(15),
                new DailyParkingChargeStrategy());
        ParkingChargeStrategy replacement = (baseRate, entryTime, exitTime, permit) ->
                new Money(7);

        lot.setChargeStrategy(replacement);

        assertSame(replacement, lot.getChargeStrategy());
        assertEquals(new Money(7), lot.calculateCharge(
                LocalDateTime.of(2026, 9, 24, 9, 0),
                LocalDateTime.of(2026, 9, 24, 10, 0), null));
    }
}