package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReceiveInvoicesSteps {

    private Customer customer;
    private Location location;
    private ChargingStation station;
    private Session session;
    private Invoice invoice;

    @Given("a finished charging session exists for customer {string}")
    public void aFinishedChargingSessionExistsForCustomer(String customerId) {
        customer = new Customer(customerId, "", "", "");
        UserManager.addUser(customer);
    }

    @Given("the session details are:")
    public void theSessionDetailsAre(DataTable dataTable) {
        List<List<String>> rows = dataTable.asLists(String.class);
        for (List<String> row : rows) {
            String key = row.get(0);
            String value = row.get(1);
            if (key.equalsIgnoreCase("Station")) {
                String[] parts = value.split("\\s+-\\s+");
                String locationName = parts[0];
                String stationId = parts.length > 1 ? parts[1] : "CS-UNKNOWN";
                location = new Location("LOC-" + locationName.replace(" ", "-"), locationName, "", Status.Active);
                station = new ChargingStation(
                        stationId,
                        StationState.inOperationFree,
                        location.getLocationId(),
                        ChargingStationType.DC,
                        new Price()
                );
                station.addChargingStation();
            } else if (key.equalsIgnoreCase("Duration")) {
                long minutes = parseMinutes(value);
                ensureSessionInitialized();
                session.setDuration(minutes);
            } else if (key.equalsIgnoreCase("Price")) {
                double rate = parsePrice(value);
                if (station == null) {
                    station = new ChargingStation(
                            "CS-DEFAULT",
                            StationState.inOperationFree,
                            "LOC-DEFAULT",
                            ChargingStationType.DC,
                            new Price()
                    );
                    station.addChargingStation();
                }
                station.setPrice(new Price(rate));
            } else if (key.equalsIgnoreCase("Total")) {
                double total = Double.parseDouble(value);
                customer.rechargeAccount(total);
                ensureSessionInitialized();
                session.endSession();
            }
        }
    }

    @When("I request the invoice for the last session")
    public void iRequestTheInvoiceForTheLastSession() {
        invoice = new Invoice("INV-" + session.getSessionId(), session.getSessionId(), session.getTotalCost());
        invoice.generateInvoice();
    }

    @Then("a new invoice should be generated")
    public void aNewInvoiceShouldBeGenerated() {
        assertTrue("GENERATED".equalsIgnoreCase(invoice.getStatus()));
    }

    @Then("the invoice should contain the location {string}")
    public void theInvoiceShouldContainTheLocation(String location) {
        assertEquals(location, this.location.getName());
    }

    @Then("the invoice should list {string} duration")
    public void theInvoiceShouldListDuration(String duration) {
        assertEquals(duration, String.format(Locale.US, "%d min", session.getDuration()));
    }

    @Then("the final amount on the invoice should be {double}")
    public void theFinalAmountOnTheInvoiceShouldBe(double amount) {
        assertEquals(amount, invoice.getTotalCost());
    }

    private void ensureSessionInitialized() {
        if (session == null) {
            session = new Session("SESSION-INV", station.getStationID(), customer);
            session.startSession();
        }
    }

    private long parseMinutes(String value) {
        return Long.parseLong(value.replace("min", "").trim());
    }

    private double parsePrice(String value) {
        String cleaned = value.toLowerCase(Locale.ROOT)
                .replace("per min", "")
                .trim();
        return Double.parseDouble(cleaned);
    }
}
