package com.example.parkingsystem.charges.factory;

import com.example.parkingsystem.charges.strategy.CompactDiscountParkingChargeStrategy;
import com.example.parkingsystem.charges.strategy.HourlyParkingChargeStrategy;
import com.example.parkingsystem.charges.strategy.ParkingChargeStrategy;
import com.example.parkingsystem.charges.strategy.SundayFreeParkingChargeStrategy;

import java.util.Objects;

public class DefaultParkingChargeStrategyFactory
        implements ParkingChargeStrategyFactory {
    private final boolean sundayIsFree;

    public DefaultParkingChargeStrategyFactory(boolean sundayIsFree) {
        this.sundayIsFree = sundayIsFree;
    }

    @Override
    public ParkingChargeStrategy createStrategy() {
        ParkingChargeStrategy strategy = new HourlyParkingChargeStrategy();
        if (sundayIsFree) {
            strategy = new SundayFreeParkingChargeStrategy(strategy);
        }
        return new CompactDiscountParkingChargeStrategy(strategy);
    }
}