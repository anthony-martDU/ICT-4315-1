package com.example.parkingsystem.charges.factory;

import com.example.parkingsystem.charges.strategy.ParkingChargeStrategy;

public interface ParkingChargeStrategyFactory {
    ParkingChargeStrategy createStrategy();
}