package edu.du.ict4315.charges.decorator;

import com.example.parkingsystem.Money;
import com.example.parkingsystem.ParkingLot;
import com.example.parkingsystem.ParkingPermit;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Objects;

public class SundayFreeParkingDecorator
        extends ParkingChargeCalculatorDecorator {
    public SundayFreeParkingDecorator(ParkingChargeCalculator component) {
        super(component);
    }

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
            if (current.getDayOfWeek() != DayOfWeek.SUNDAY) {
                total = total.add(getComponent().getParkingCharge(current,
                        segmentEnd, parkingLot, permit));
            }
            current = segmentEnd;
        }
        return total;
    }
}