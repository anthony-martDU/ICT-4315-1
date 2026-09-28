package com.example.parkingsystem.charges.strategy;

import com.example.parkingsystem.CarType;
import com.example.parkingsystem.Money;
import com.example.parkingsystem.ParkingPermit;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class CompactDiscountParkingChargeStrategy implements ParkingChargeStrategy {
    private static final BigDecimal COMPACT_RATE = new BigDecimal("0.80");
    private final ParkingChargeStrategy regularStrategy;

    public CompactDiscountParkingChargeStrategy(
            ParkingChargeStrategy regularStrategy) {
        this.regularStrategy = Objects.requireNonNull(regularStrategy,
                "regularStrategy");
    }

    @Override
    public Money calculateCharge(Money baseRate, LocalDateTime entryTime,
                                 LocalDateTime exitTime, ParkingPermit permit) {
        Objects.requireNonNull(permit, "permit");
        Money charge = regularStrategy.calculateCharge(baseRate, entryTime,
                exitTime, permit);
        if (permit.getCar().getType() == CarType.COMPACT) {
            return charge.multiply(COMPACT_RATE);
        }
        return charge;
    }
}