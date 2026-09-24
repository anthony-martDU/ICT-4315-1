package com.example.parkingsystem.charges.strategy;

import com.example.parkingsystem.Money;
import com.example.parkingsystem.ParkingPermit;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class HourlyParkingChargeStrategy implements ParkingChargeStrategy {
    private static final BigDecimal DAILY_MAXIMUM = new BigDecimal("15.00");
    private static final long MILLIS_PER_HOUR = 60 * 60 * 1000;

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
            total = total.add(dailyCharge(baseRate, current, segmentEnd));
            current = segmentEnd;
        }
        return total;
    }

    private Money dailyCharge(Money baseRate, LocalDateTime start,
                              LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        long milliseconds = duration.toMillis();
        long hours = (milliseconds + MILLIS_PER_HOUR - 1) / MILLIS_PER_HOUR;
        BigDecimal charge = baseRate.getAmount()
                .multiply(BigDecimal.valueOf(hours))
                .min(DAILY_MAXIMUM);
        return new Money(charge);
    }
}