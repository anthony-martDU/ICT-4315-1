package com.example.parkingsystem;

import com.example.parkingsystem.charges.strategy.HourlyParkingChargeStrategy;
import com.example.parkingsystem.charges.strategy.CompactDiscountParkingChargeStrategy;
import com.example.parkingsystem.charges.strategy.ParkingChargeStrategyFactory;
import com.example.parkingsystem.charges.strategy.SundayFreeParkingChargeStrategy;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParkingChargeStrategyTest {
    @Test
        void hourlyStrategyRoundsPartialHoursUp() {
                ParkingPermit permit = permitFor(CarType.COMPACT);
                HourlyParkingChargeStrategy strategy = new HourlyParkingChargeStrategy();

                Money charge = strategy.calculateCharge(new Money(2),
                                LocalDateTime.of(2026, 9, 24, 9, 0),
                                LocalDateTime.of(2026, 9, 24, 10, 1), permit);

                assertEquals(new Money(4), charge);
    }

    @Test
        void hourlyStrategyAppliesDailyMaximum() {
        ParkingPermit permit = permitFor(CarType.SUV);
                HourlyParkingChargeStrategy strategy = new HourlyParkingChargeStrategy();

                Money charge = strategy.calculateCharge(new Money(2),
                                LocalDateTime.of(2026, 9, 24, 0, 0),
                                LocalDateTime.of(2026, 9, 24, 10, 0), permit);

        assertEquals(new Money(15), charge);
    }

    @Test
    void compactDiscountIsAppliedAfterDailyMaximum() {
        ParkingPermit permit = permitFor(CarType.COMPACT);
        CompactDiscountParkingChargeStrategy strategy =
                new CompactDiscountParkingChargeStrategy(
                        new HourlyParkingChargeStrategy());

        Money charge = strategy.calculateCharge(new Money(2),
                LocalDateTime.of(2026, 9, 24, 0, 0),
                LocalDateTime.of(2026, 9, 24, 10, 0), permit);

        assertEquals(new Money(12), charge);
    }

    @Test
    void suvIsNotDiscounted() {
        ParkingPermit permit = permitFor(CarType.SUV);
        CompactDiscountParkingChargeStrategy strategy =
                new CompactDiscountParkingChargeStrategy(
                        new HourlyParkingChargeStrategy());

        Money charge = strategy.calculateCharge(new Money(2),
                LocalDateTime.of(2026, 9, 24, 0, 0),
                LocalDateTime.of(2026, 9, 24, 10, 0), permit);

        assertEquals(new Money(15), charge);
    }

    @Test
    void sundayStrategyReturnsNoChargeOnSunday() {
        ParkingPermit permit = permitFor(CarType.SUV);
        SundayFreeParkingChargeStrategy strategy =
                new SundayFreeParkingChargeStrategy();

        Money charge = strategy.calculateCharge(new Money(2),
                LocalDateTime.of(2026, 9, 27, 9, 0),
                LocalDateTime.of(2026, 9, 27, 12, 0), permit);

        assertEquals(new Money(0), charge);
    }

    @Test
    void sundayStrategyDelegatesToRegularStrategyOnOtherDays() {
        ParkingPermit permit = permitFor(CarType.COMPACT);
        SundayFreeParkingChargeStrategy strategy =
                new SundayFreeParkingChargeStrategy(new HourlyParkingChargeStrategy());

        Money charge = strategy.calculateCharge(new Money(2),
                LocalDateTime.of(2026, 9, 24, 9, 0),
                LocalDateTime.of(2026, 9, 24, 10, 0), permit);

        assertEquals(new Money(2), charge);
    }

    @Test
    void factoryBuildsSundayFreeStrategyWithCompactDiscount() {
        ParkingPermit permit = permitFor(CarType.COMPACT);
        ParkingChargeStrategyFactory factory = new ParkingChargeStrategyFactory();

        Money charge = factory.create(true).calculateCharge(new Money(2),
                LocalDateTime.of(2026, 9, 24, 9, 0),
                LocalDateTime.of(2026, 9, 24, 10, 0), permit);

        assertEquals(new Money(1.60), charge);
    }

    @Test
    void factoryStillMakesSundayFreeForCompactCars() {
        ParkingPermit permit = permitFor(CarType.COMPACT);
        ParkingChargeStrategyFactory factory = new ParkingChargeStrategyFactory();

        Money charge = factory.create(true).calculateCharge(new Money(2),
                LocalDateTime.of(2026, 9, 27, 9, 0),
                LocalDateTime.of(2026, 9, 27, 10, 0), permit);

        assertEquals(new Money(0), charge);
    }

    private ParkingPermit permitFor(CarType type) {
        Customer owner = new Customer("customer-1", "John", "Smith",
                "555-0100", null);
        return new ParkingPermit("permit-1", new Car(type, "ABC-123", owner));
    }
}