package org.example;

public class ElectricChargingStationNetwork {
    public static void main(String[] args) {

        //Owner Story
        Owner owner = new Owner("OWN123", "Own Person", "o@o.com", "abcdefgh");
        //Configure Locations
        Location loc = new Location("LOC-1", "Vienna-Central", "Stephansplatz 1", Status.Active); //adds also location to list
        //Configure Charging Stations + Define Price
        ChargingStation station = new ChargingStation("CS-01", StationState.inOperationFree, "LOC-1", ChargingStationType.AC, new Price(0.50));
        //Generate Invoice list
        Invoice inv = new Invoice("INV-01", new Customer("USER123", "123", "u@u.com", "12345678"));
        for (Invoice i : InvoiceList.invoices) {
            System.out.println(i.toString());
        }
        //Gather network data
        System.out.println(loc.getLocationInfo());

        System.out.println();

        //Customer Story
        //registration of a new customer
        Customer customer1 = new Customer("User001", "Test Person", "a@a.com", "12345678");
        customer1.register();
        //payment to top up balance
        customer1.rechargeAccount(100);
        //check the current available balance
        System.out.println(customer1.getCustomerInfo());
    }
}
