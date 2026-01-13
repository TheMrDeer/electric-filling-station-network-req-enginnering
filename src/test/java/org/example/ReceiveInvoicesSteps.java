package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReceiveInvoicesSteps {

    private String customerId;
    private String sessionLocation;
    private String sessionDuration;
    private double sessionTotalAmount;

    private boolean invoiceGenerated;
    private String invoiceLocation;
    private String invoiceDuration;
    private double invoiceAmount;

    @Given("a finished charging session exists for customer {string}")
    public void aFinishedChargingSessionExistsForCustomer(String customerId) {
        this.customerId = customerId;
    }

    @Given("the session details are:")
    public void theSessionDetailsAre(DataTable dataTable) {
        List<List<String>> rows = dataTable.asLists(String.class);
        for (List<String> row : rows) {
            String key = row.get(0);
            String value = row.get(1);
            if (key.equalsIgnoreCase("Station")) {
                sessionLocation = parseLocation(value);
            } else if (key.equalsIgnoreCase("Duration")) {
                sessionDuration = value;
            } else if (key.equalsIgnoreCase("Total")) {
                sessionTotalAmount = Double.parseDouble(value);
            }
        }
    }

    @When("I request the invoice for the last session")
    public void iRequestTheInvoiceForTheLastSession() {
        invoiceGenerated = true;
        invoiceLocation = sessionLocation;
        invoiceDuration = sessionDuration;
        invoiceAmount = sessionTotalAmount;
    }

    @Then("a new invoice should be generated")
    public void aNewInvoiceShouldBeGenerated() {
        assertTrue(invoiceGenerated);
    }

    @Then("the invoice should contain the location {string}")
    public void theInvoiceShouldContainTheLocation(String location) {
        assertEquals(location, invoiceLocation);
    }

    @Then("the invoice should list {string} duration")
    public void theInvoiceShouldListDuration(String duration) {
        assertEquals(duration, invoiceDuration);
    }

    @Then("the final amount on the invoice should be {double}")
    public void theFinalAmountOnTheInvoiceShouldBe(double amount) {
        assertEquals(amount, invoiceAmount);
    }

    private String parseLocation(String stationValue) {
        String[] parts = stationValue.split("\\s+-\\s+");
        return parts.length > 0 ? parts[0] : stationValue;
    }
}
