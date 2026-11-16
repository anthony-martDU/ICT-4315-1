package com.example.parkingsystem;

import com.example.parkingsystem.protocol.ParkingRequest;
import com.example.parkingsystem.protocol.ParkingRequestProcessor;
import com.example.parkingsystem.protocol.ParkingResponse;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class DependencyInjectionTest {
    @Test
    void injectedServerComponentsShareTheSameParkingOffice() {
        Injector injector = Guice.createInjector(new ParkingSystemModule());
        ParkingOffice office = injector.getInstance(ParkingOffice.class);
        ParkingService service = injector.getInstance(ParkingService.class);
        ParkingRequestProcessor processor =
                injector.getInstance(ParkingRequestProcessor.class);

        assertSame(office, injector.getInstance(ParkingOffice.class));

        Properties customerParameters = new Properties();
        customerParameters.setProperty("name", "Rob Smith");
        customerParameters.setProperty("address", "123 Main St");
        ParkingResponse response = processor.process(
                new ParkingRequest("CUSTOMER", customerParameters).toJson());

        assertEquals(200, response.getStatusCode());
        assertEquals("customer-1", response.getMessage());
        assertEquals("permit-1", service.performCommand("CAR", new String[]{
                "licensePlate=ROB4CO",
                "type=SUV",
                "customerId=" + response.getMessage()
        }));
        assertEquals("Rob Smith",
                office.getCustomer("customer-1").getCustomerName());
    }

    @Test
    void testInjectorCanProvideAnIsolatedParkingOffice() {
        ParkingOffice testOffice = new ParkingOffice();
        Injector injector = Guice.createInjector(binder ->
                binder.bind(ParkingOffice.class).toInstance(testOffice));

        ParkingService service = injector.getInstance(ParkingService.class);

        assertEquals("customer-1", service.performCommand("CUSTOMER", new String[]{
                "name=Test Customer",
                "address=456 Test St"
        }));
        assertEquals("Test Customer",
                testOffice.getCustomer("customer-1").getCustomerName());
    }
}
