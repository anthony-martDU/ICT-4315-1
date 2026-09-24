package com.example.parkingsystem.charges.strategy;

import com.example.parkingsystem.CarType;
import com.example.parkingsystem.Money;
import com.example.parkingsystem.ParkingPermit;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class DailyParkingChargeStrategy implements ParkingChargeStrategy {
    private static final BigDecimal COMPACT_DISCOUNT = new BigDecimal("0.80");

    @Override
    public Money calculateCharge(Money baseRate, LocalDateTime entryTime,
                                 LocalDateTime exitTime, ParkingPermit permit) {
        Objects.requireNonNull(baseRate, "baseRate");
        Objects.requireNonNull(entryTime, "entryTime");
        Objects.requireNonNull(exitTime, "exitTime");
        Objects.requireNonNull(permit, "permit");

        if (permit.getCar().getType() == CarType.COMPACT) {
            return baseRate.multiply(COMPACT_DISCOUNT);
        }
        return baseRate;
    }
}