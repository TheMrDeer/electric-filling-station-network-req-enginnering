package org.example;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

public class ElectricChargingStationNetwork {
    public static void main(String[] args) {

        //Owner Story
        Owner owner = new Owner("OWN123", "Own Person", "o@o.com", "abcdefgh");

        //Configure Locations, adds also locations to Stationmanager
        Location loc = new Location("LOC-1", "Vienna-Central", "Stephansplatz 1", Status.Active);
        Location loc2 = new Location("LOC-2", "Graz-North", "Hauptplatz 1", Status.Inactive);

        //Configure Charging Stations + Define Prices
        ChargingStation station = new ChargingStation("CS-01", StationState.inOperationFree, "LOC-1", ChargingStationType.AC, new Price(0.5));
        station.addChargingStation();
        loc.addPrice(new Price("LOC-1", ChargingStationType.AC, 0.50, 1.00, now().minusDays(1), null));
        loc.addPrice(new Price("LOC-1", ChargingStationType.DC, 0.30, 0.70, now().minusDays(1), null));

        //Generate Invoice list
        Invoice inv = new Invoice("INV-01", new Customer("USER123", "123", "u@u.com", "12345678"));
        for (Invoice i : InvoiceList.invoices) {
            System.out.println(i.toString());
        }

        //Gather network data
        for (Location l : StationManager.getInstance().getLocations()) {
            System.out.println(l.getLocationInfo());
        }

        System.out.println();

        //Customer Story
        //registration of a new customer
        Customer customer1 = new Customer("User001", "Test Person", "a@a.com", "12345678");
        customer1.register();

        //payment to top up balance
        customer1.rechargeAccount(100);

        //Check the current available balance
        System.out.println(customer1.getCustomerInfo());

        //See available charging stations and prices
        StationManager sm = StationManager.getInstance();
        LocalDateTime now = now();

        for (Location location : sm.getLocations()) {
            System.out.println(location.getLocationInfo());
            for (ChargingStationType t : ChargingStationType.values()) {
                Price p = location.getPriceFor(t, now);
                if (p != null) {
                    System.out.println("  " + t + ": " + p.getPricePerMinute() + "/min, " + p.getPricePerKwh() + "/kWh");
                } else {
                    System.out.println("  " + t + ": no price set");
                }
            }
            System.out.println();
        }

        //Generate a session and invoice
        Session session = new Session(
                "SES-01",
                station.getStationID(),
                customer1
        );

        session.startSession();
        session.setChargedEnergy(20);   // kWh
        session.setDuration(40);        // Minutes
        session.endSession();

        Invoice invoice = new Invoice(
                "INV-01",
                customer1
        );

        invoice.addSession(session);
        System.out.println(invoice.toString());
    }
}
