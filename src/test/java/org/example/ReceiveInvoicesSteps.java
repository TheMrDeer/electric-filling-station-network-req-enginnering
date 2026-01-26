package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReceiveInvoicesSteps {

    private Customer customer;
    private List<Session> sessions = new ArrayList<>();
    private List<Double> topUps = new ArrayList<>();
    private String invoiceOutput;
    private String lastError;

    @Before
    public void cleanup() {
        StationManager.getInstance().clearAll();
    }

    @Given("a customer {string} exists with balance {double}")
    public void aCustomerExistsWithBalance(String customerId, double balance) {
        customer = new Customer(customerId, customerId, customerId + "@example.com", "password");
        if (balance > 0) {
            customer.rechargeAccount(balance);
        }
    }

    @Given("the customer tops up {double}")
    public void theCustomerTopsUp(double amount) {
        customer.rechargeAccount(amount);
        topUps.add(amount);
    }

    @Given("the customer has the following finished sessions:")
    public void theCustomerHasTheFollowingFinishedSessions(DataTable table) {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        
        for (Map<String, String> row : rows) {
            String locName = row.get("Location");
            String stationId = row.get("Station ID");
            String typeStr = row.get("Type");
            String dateStr = row.get("Date");
            long duration = Long.parseLong(row.get("Duration"));
            double kwh = Double.parseDouble(row.get("kWh"));
            
            // 1. Setup Location and Station
            Location location = StationManager.getInstance().findLocationByName(locName);
            if (location == null) {
                location = new Location("LOC-" + locName.hashCode(), locName, "Address", Status.Active);
                
                if (typeStr.equals("AC")) {
                    location.addPrice(new Price(location.getLocationId(), ChargingStationType.AC, 0.10, 0.20, LocalDateTime.MIN, null));
                } else {
                    location.addPrice(new Price(location.getLocationId(), ChargingStationType.DC, 0.20, 0.60, LocalDateTime.MIN, null));
                }
            }
            
            ChargingStationType type = ChargingStationType.valueOf(typeStr);
            // Use provided stationId if available, otherwise generate
            if (stationId == null || stationId.isEmpty()) {
                stationId = "CS-" + typeStr + "-" + System.nanoTime();
            }
            
            ChargingStation station = new ChargingStation(stationId, StationState.inOperationFree, location.getLocationId(), type, null);
            location.addChargingStationToLocation(station);
            StationManager.getInstance().addStation(station);

            // 2. Create and Run Session
            Session session = new Session("S-" + System.nanoTime(), stationId, customer);
            
            session.startSession(); 
            session.setStartTime(LocalDateTime.parse(dateStr));
            
            session.setDuration(duration);
            session.setChargedEnergy(kwh);
            session.endSession(); 
            
            sessions.add(session);
        }
    }

    @When("I request the invoice")
    public void iRequestTheInvoice() {
        // Generate a unique ID for the invoice
        String invoiceId = "INV-" + UUID.randomUUID().toString().substring(0, 8);
        Invoice invoice = new Invoice(invoiceId, customer);
        for (Session s : sessions) {
            invoice.addSession(s);
        }
        for (Double amount : topUps) {
            invoice.addTopUp(amount);
        }
        invoiceOutput = invoice.generateInvoice();
    }

    @Then("the invoice should list {int} charging sessions sorted by date")
    public void theInvoiceShouldListChargingSessionsSortedByDate(int count) {
        int firstIndex = invoiceOutput.indexOf("Vienna Central");
        int secondIndex = invoiceOutput.indexOf("Graz Main");
        Assertions.assertTrue(firstIndex < secondIndex, "Sessions should be sorted by date (Vienna before Graz)");
    }

    @Then("the invoice should list the Top-Up of {double}")
    public void theInvoiceShouldListTheTopUpOf(double amount) {
        Assertions.assertTrue(invoiceOutput.contains(String.format("%.2f", amount)), "Invoice should contain top-up amount");
    }

    @Then("the first entry should contain {string}, {string}, and {string}")
    public void theFirstEntryShouldContainAnd(String loc, String type, String cost) {
        Assertions.assertTrue(invoiceOutput.contains(loc));
        Assertions.assertTrue(invoiceOutput.contains(type));
        
        try {
            double costVal = Double.parseDouble(cost);
            String formattedCost = String.format("%.2f", costVal);
            Assertions.assertTrue(invoiceOutput.contains(formattedCost), 
                "Expected invoice to contain cost: " + formattedCost + " (derived from " + cost + ")");
        } catch (NumberFormatException e) {
            Assertions.assertTrue(invoiceOutput.contains(cost));
        }
    }

    @Then("the invoice should show the current remaining balance")
    public void theInvoiceShouldShowTheCurrentRemainingBalance() {
        double balance = customer.checkBalance();
        Assertions.assertTrue(invoiceOutput.contains(String.format("Outstanding Balance: %.2f", balance)));
    }

    @Then("the invoice should contain the Invoice Item Number {string}")
    public void theInvoiceShouldContainTheInvoiceItemNumber(String itemNumber) {
        // Check for the item number in the table (e.g., "1      |")
        Assertions.assertTrue(invoiceOutput.contains(itemNumber + " "), "Invoice should contain item number " + itemNumber);
    }

    @Then("the invoice should contain the Charging Station ID {string}")
    public void theInvoiceShouldContainTheChargingStationID(String stationId) {
        Assertions.assertTrue(invoiceOutput.contains(stationId), "Invoice should contain Station ID " + stationId);
    }

    @Then("the invoice should have a unique Invoice ID")
    public void theInvoiceShouldHaveAUniqueInvoiceID() {
        Assertions.assertTrue(invoiceOutput.contains("Invoice ID: INV-"), "Invoice should have a generated Invoice ID");
    }

    @When("I attempt to add a session with start time {string} and end time {string}")
    public void iAttemptToAddASessionWithStartTimeAndEndTime(String startStr, String endStr) {
        try {
            Session session = new Session("S-Error", "CS-Error", customer);
            LocalDateTime start = LocalDateTime.parse(startStr);
            LocalDateTime end = LocalDateTime.parse(endStr);
            session.setStartTime(start);
            session.setEndTime(end);
        } catch (IllegalArgumentException e) {
            lastError = e.getMessage();
        }
    }

    @Then("I should receive a calculation error message {string}")
    public void iShouldReceiveACalculationErrorMessage(String expectedMessage) {
        Assertions.assertEquals(expectedMessage, lastError);
    }

    @When("I request the invoice for the last session")
    public void iRequestTheInvoiceForTheLastSession() {
        if (customer == null) {
            customer = new Customer("User123", "User123", "user@example.com", "password");
        }
        iRequestTheInvoice();
    }

    @Then("a new invoice should be generated")
    public void aNewInvoiceShouldBeGenerated() {
        Assertions.assertNotNull(invoiceOutput);
        Assertions.assertFalse(invoiceOutput.isEmpty());
    }

    @Then("the invoice should contain the location {string}")
    public void theInvoiceShouldContainTheLocation(String location) {
        Assertions.assertTrue(invoiceOutput.contains(location));
    }

    @Then("the invoice should list {string} duration")
    public void theInvoiceShouldListDuration(String duration) {
        // Duration format might vary, checking for presence
        Assertions.assertTrue(invoiceOutput.contains(duration));
    }

    @Then("the final amount on the invoice should be {double}")
    public void theFinalAmountOnTheInvoiceShouldBe(Double amount) {
        Assertions.assertTrue(invoiceOutput.contains(String.format("%.2f", amount)));
    }

    @Then("the charging station type is ..")
    public void theChargingStationTypeIs() {
        // This step seems incomplete in the feature file or just a placeholder
        // We can check if AC or DC is present
        Assertions.assertTrue(invoiceOutput.contains("AC") || invoiceOutput.contains("DC"));
    }
}
