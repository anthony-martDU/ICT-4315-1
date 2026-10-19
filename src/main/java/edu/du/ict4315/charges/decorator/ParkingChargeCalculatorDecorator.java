package edu.du.ict4315.charges.decorator;

import java.util.Objects;

public abstract class ParkingChargeCalculatorDecorator
        extends ParkingChargeCalculator {
    private final ParkingChargeCalculator component;

    protected ParkingChargeCalculatorDecorator(
            ParkingChargeCalculator component) {
        this.component = Objects.requireNonNull(component, "component");
    }

    protected final ParkingChargeCalculator getComponent() {
        return component;
    }
}