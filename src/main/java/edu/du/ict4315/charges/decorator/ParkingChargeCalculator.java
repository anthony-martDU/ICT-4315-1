package edu.du.ict4315.charges.decorator;

import com.example.parkingsystem.Money;
import com.example.parkingsystem.ParkingLot;
import com.example.parkingsystem.ParkingPermit;

import java.time.LocalDateTime;

public abstract class ParkingChargeCalculator {
    protected ParkingChargeCalculator() {
    }

    public abstract Money getParkingCharge(LocalDateTime entryTime,
            LocalDateTime exitTime, ParkingLot parkingLot,
            ParkingPermit permit);
}