package edu.du.ict4315.charges.decorator;

import com.example.parkingsystem.CarType;
import com.example.parkingsystem.Money;
import com.example.parkingsystem.ParkingLot;
import com.example.parkingsystem.ParkingPermit;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class CompactCarDiscountDecorator
        extends ParkingChargeCalculatorDecorator {
    private static final BigDecimal COMPACT_RATE = new BigDecimal("0.80");

    public CompactCarDiscountDecorator(ParkingChargeCalculator component) {
        super(component);
    }

    @Override
    public Money getParkingCharge(LocalDateTime entryTime,
            LocalDateTime exitTime, ParkingLot parkingLot,
            ParkingPermit permit) {
        Objects.requireNonNull(permit, "permit");
        Money charge = getComponent().getParkingCharge(entryTime, exitTime,
                parkingLot, permit);
        if (permit.getCar().getType() == CarType.COMPACT) {
            return charge.multiply(COMPACT_RATE);
        }
        return charge;
    }
}