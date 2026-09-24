package com.example.parkingsystem.charges.strategy;

import com.example.parkingsystem.Money;
import com.example.parkingsystem.ParkingPermit;

import java.time.LocalDateTime;

public interface ParkingChargeStrategy {
    Money calculateCharge(Money baseRate, LocalDateTime entryTime,
                          LocalDateTime exitTime, ParkingPermit permit);
}