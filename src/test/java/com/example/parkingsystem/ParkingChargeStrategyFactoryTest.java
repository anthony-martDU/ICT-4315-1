package com.example.parkingsystem;

import edu.du.ict4315.charges.decorator.DefaultParkingChargeCalculatorFactory;
import edu.du.ict4315.charges.decorator.ParkingChargeCalculator;
import edu.du.ict4315.charges.decorator.ParkingChargeCalculatorFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ParkingChargeStrategyFactoryTest {
    @Test
    void createsCalculatorInstances() {
        ParkingChargeCalculatorFactory factory =
                new DefaultParkingChargeCalculatorFactory(true);

        ParkingChargeCalculator first = factory.createCalculator();
        ParkingChargeCalculator second = factory.createCalculator();

        assertNotNull(first);
        assertNotSame(first, second);
    }
}