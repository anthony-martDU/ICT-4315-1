package com.example.parkingsystem;

import com.example.parkingsystem.charges.strategy.ParkingChargeStrategyFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParkingChargeServiceTest {
    @Test
    void delegatesChargeCalculationToParkingLotStrategy() {
        ParkingLot lot = new ParkingLot("Main Lot", new Money(2),
                new ParkingChargeStrategyFactory().create(true));
        ParkingPermit permit = new ParkingPermit("permit-1",
                new Car(CarType.COMPACT, "ABC-123", null));

        Money charge = new ParkingChargeService().calculateCharge(lot, permit,
                LocalDateTime.of(2026, 9, 24, 9, 0),
                LocalDateTime.of(2026, 9, 24, 10, 0));

        assertEquals(new Money(1.60), charge);
    }
}