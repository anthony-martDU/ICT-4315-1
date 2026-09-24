package com.example.parkingsystem.charges.strategy;

public class ParkingChargeStrategyFactory {
    public ParkingChargeStrategy create(boolean sundayIsFree) {
        ParkingChargeStrategy hourly = new HourlyParkingChargeStrategy();
        ParkingChargeStrategy dayStrategy = sundayIsFree
                ? new SundayFreeParkingChargeStrategy(hourly)
                : hourly;
        return new SuvDiscountParkingChargeStrategy(dayStrategy);
    }
}