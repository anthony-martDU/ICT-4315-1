package com.example.parkingsystem;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class ParkingSystemModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ParkingOffice.class).in(Scopes.SINGLETON);
    }
}
