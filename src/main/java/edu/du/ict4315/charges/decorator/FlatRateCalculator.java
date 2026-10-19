package edu.du.ict4315.charges.decorator;

import com.example.parkingsystem.Money;
import com.example.parkingsystem.ParkingLot;
import com.example.parkingsystem.ParkingPermit;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class FlatRateCalculator extends ParkingChargeCalculator {
    private static final BigDecimal DAILY_MAXIMUM = new BigDecimal("15.00");

    @Override
    public Money getParkingCharge(LocalDateTime entryTime,
            LocalDateTime exitTime, ParkingLot parkingLot,
            ParkingPermit permit) {
        Objects.requireNonNull(entryTime, "entryTime");
        Objects.requireNonNull(exitTime, "exitTime");
        Objects.requireNonNull(parkingLot, "parkingLot");
        Objects.requireNonNull(permit, "permit");
        if (exitTime.isBefore(entryTime)) {
            throw new IllegalArgumentException(
                    "Exit time cannot be before entry time");
        }

        Money total = new Money(0);
        LocalDateTime current = entryTime;
        while (current.isBefore(exitTime)) {
            LocalDateTime midnight = current.toLocalDate().plusDays(1)
                    .atStartOfDay();
            LocalDateTime segmentEnd = exitTime.isBefore(midnight)
                    ? exitTime : midnight;
            total = total.add(chargeForDay(parkingLot.getBaseRate(), current,
                    segmentEnd));
            current = segmentEnd;
        }
        return total;
    }

    private Money chargeForDay(Money baseRate, LocalDateTime start,
                               LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        long hours = duration.toHours();
        if (!duration.minusHours(hours).isZero()) {
            hours++;
        }
        BigDecimal charge = baseRate.getAmount()
                .multiply(BigDecimal.valueOf(hours))
                .min(DAILY_MAXIMUM);
        return new Money(charge);
    }
}