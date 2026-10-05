package com.example.parkingsystem;

import com.example.parkingsystem.charges.factory.DefaultParkingChargeStrategyFactory;
import com.example.parkingsystem.charges.factory.ParkingChargeStrategyFactory;
import com.example.parkingsystem.charges.strategy.ParkingChargeStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ParkingChargeStrategyFactoryTest {
    @Test
    void createsStrategyInstances() {
        ParkingChargeStrategyFactory factory =
                new DefaultParkingChargeStrategyFactory(true);

        ParkingChargeStrategy first = factory.createStrategy();
        ParkingChargeStrategy second = factory.createStrategy();

        assertNotNull(first);
        assertNotSame(first, second);
    }
}