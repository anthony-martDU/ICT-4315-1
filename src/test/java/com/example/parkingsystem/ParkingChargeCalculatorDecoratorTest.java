package com.example.parkingsystem;

import edu.du.ict4315.charges.decorator.CompactCarDiscountDecorator;
import edu.du.ict4315.charges.decorator.DefaultParkingChargeCalculatorFactory;
import edu.du.ict4315.charges.decorator.FlatRateCalculator;
import edu.du.ict4315.charges.decorator.ParkingChargeCalculator;
import edu.du.ict4315.charges.decorator.SundayFreeParkingDecorator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParkingChargeCalculatorDecoratorTest {
    private final LocalDateTime entry = LocalDateTime.of(2026, 9, 24, 9, 0);
    private final LocalDateTime exit = LocalDateTime.of(2026, 9, 24, 10, 1);

    @Test
    void flatRateCalculatorRoundsPartialHoursUpAndAppliesDailyCap() {
        ParkingLot lot = lot(false);

        assertEquals(new Money(4), new FlatRateCalculator()
                .getParkingCharge(entry, exit, lot, permit(CarType.SUV)));
        assertEquals(new Money(15), new FlatRateCalculator()
                .getParkingCharge(entry, LocalDateTime.of(2026, 9, 24, 19, 0),
                        lot, permit(CarType.SUV)));
    }

    @Test
    void compactDecoratorDiscountsCompactButNotSuv() {
        ParkingChargeCalculator calculator = new CompactCarDiscountDecorator(
                new FlatRateCalculator());

        assertEquals(new Money(3.20), calculator.getParkingCharge(entry, exit,
                lot(false), permit(CarType.COMPACT)));
        assertEquals(new Money(4), calculator.getParkingCharge(entry, exit,
                lot(false), permit(CarType.SUV)));
    }

    @Test
    void sundayDecoratorSkipsSundaySegmentsAndChargesSaturdaySegments() {
        ParkingChargeCalculator calculator = new SundayFreeParkingDecorator(
                new FlatRateCalculator());
        LocalDateTime saturdayNight = LocalDateTime.of(2026, 9, 26, 23, 0);
        LocalDateTime sundayMorning = LocalDateTime.of(2026, 9, 27, 1, 0);

        assertEquals(new Money(2), calculator.getParkingCharge(saturdayNight,
                sundayMorning, lot(false), permit(CarType.SUV)));
        assertEquals(new Money(0), calculator.getParkingCharge(
                LocalDateTime.of(2026, 9, 27, 9, 0),
                LocalDateTime.of(2026, 9, 27, 12, 0), lot(false),
                permit(CarType.SUV)));
    }

    @Test
    void decoratorsComposeAndMayBeAppliedMoreThanOnce() {
        ParkingChargeCalculator calculator = new CompactCarDiscountDecorator(
                new CompactCarDiscountDecorator(new FlatRateCalculator()));

        assertEquals(new Money(2.56), calculator.getParkingCharge(entry, exit,
                lot(false), permit(CarType.COMPACT)));
    }

    @Test
    void factoryCreatesFreshCalculatorWithConfiguredDecorators() {
        DefaultParkingChargeCalculatorFactory factory =
                new DefaultParkingChargeCalculatorFactory(true);
        ParkingChargeCalculator first = factory.createCalculator();
        ParkingChargeCalculator second = factory.createCalculator();

        assertNotNull(first);
        assertNotSame(first, second);
        assertEquals(new Money(0), first.getParkingCharge(
                LocalDateTime.of(2026, 9, 27, 9, 0),
                LocalDateTime.of(2026, 9, 27, 10, 0), lot(true),
                permit(CarType.COMPACT)));
        assertEquals(new Money(3.20), first.getParkingCharge(entry, exit,
                lot(true), permit(CarType.COMPACT)));
    }

    @Test
    void flatRateCalculatorRejectsExitBeforeEntry() {
        assertThrows(IllegalArgumentException.class,
                () -> new FlatRateCalculator().getParkingCharge(exit, entry,
                        lot(false), permit(CarType.SUV)));
    }

    private ParkingLot lot(boolean sundayIsFree) {
        return new ParkingLot("Main Lot", new Money(2),
                new DefaultParkingChargeCalculatorFactory(sundayIsFree));
    }

    private ParkingPermit permit(CarType type) {
        return new ParkingPermit("permit-1", new Car(type, "ABC-123", null));
    }
}