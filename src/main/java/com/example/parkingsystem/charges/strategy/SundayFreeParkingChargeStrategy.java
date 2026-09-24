package com.example.parkingsystem.charges.strategy;

import com.example.parkingsystem.Money;
import com.example.parkingsystem.ParkingPermit;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Objects;

public class SundayFreeParkingChargeStrategy implements ParkingChargeStrategy {
    private final ParkingChargeStrategy regularStrategy;

    public SundayFreeParkingChargeStrategy() {
        this(new DailyParkingChargeStrategy());
    }

    public SundayFreeParkingChargeStrategy(ParkingChargeStrategy regularStrategy) {
        this.regularStrategy = Objects.requireNonNull(regularStrategy,
                "regularStrategy");
    }

    @Override
    public Money calculateCharge(Money baseRate, LocalDateTime entryTime,
                                 LocalDateTime exitTime, ParkingPermit permit) {
        Objects.requireNonNull(baseRate, "baseRate");
        Objects.requireNonNull(entryTime, "entryTime");
        Objects.requireNonNull(exitTime, "exitTime");
        Objects.requireNonNull(permit, "permit");
        if (exitTime.isBefore(entryTime)) {
            throw new IllegalArgumentException("Exit time cannot be before entry time");
        }

        Money total = new Money(0);
        LocalDateTime current = entryTime;
        while (current.isBefore(exitTime)) {
            LocalDateTime nextDay = current.toLocalDate().plusDays(1)
                    .atStartOfDay();
            LocalDateTime segmentEnd = exitTime.isBefore(nextDay)
                    ? exitTime : nextDay;
            if (current.getDayOfWeek() != DayOfWeek.SUNDAY) {
                total = total.add(regularStrategy.calculateCharge(baseRate,
                        current, segmentEnd, permit));
            }
            current = segmentEnd;
        }
        return total;
    }
}