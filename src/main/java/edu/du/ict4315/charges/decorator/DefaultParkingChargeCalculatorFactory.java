package edu.du.ict4315.charges.decorator;

public class DefaultParkingChargeCalculatorFactory
        implements ParkingChargeCalculatorFactory {
    private final boolean sundayIsFree;

    public DefaultParkingChargeCalculatorFactory(boolean sundayIsFree) {
        this.sundayIsFree = sundayIsFree;
    }

    @Override
    public ParkingChargeCalculator createCalculator() {
        ParkingChargeCalculator calculator = new FlatRateCalculator();
        if (sundayIsFree) {
            calculator = new SundayFreeParkingDecorator(calculator);
        }
        return new CompactCarDiscountDecorator(calculator);
    }
}