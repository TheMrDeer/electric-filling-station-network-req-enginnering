package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ManageInvoicesSteps {

    private final Map<String, Invoice> invoicesById = new HashMap<>();
    private final Map<String, Session> sessionsByInvoiceId = new HashMap<>();
    private List<Invoice> viewedInvoices = new ArrayList<>();

    @Given("the following invoices exist in the system:")
    public void theFollowingInvoicesExistInTheSystem(DataTable dataTable) {
        invoicesById.clear();
        sessionsByInvoiceId.clear();
        for (Map<String, String> row : dataTable.asMaps(String.class, String.class)) {
            String invoiceId = row.get("InvoiceID");
            String customerId = row.get("CustomerID");
            String locationName = row.get("Location");
            double totalAmount = Double.parseDouble(row.get("TotalAmount"));
            String status = row.get("Status");

            Customer customer = new Customer(customerId, "", "", "");
            UserManager.addUser(customer);
            customer.rechargeAccount(totalAmount);

            Location location = new Location("LOC-" + invoiceId, locationName, "", Status.Active);
            ChargingStation station = new ChargingStation(
                    "ST-" + invoiceId,
                    StationState.inOperationFree,
                    location.getLocationId(),
                    ChargingStationType.AC,
                    new Price(1.0)
            );
            station.addChargingStation();

            Session session = new Session("SESSION-" + invoiceId, station.getStationID(), customer);
            session.startSession();
            session.setDuration((long) totalAmount);
            session.endSession();

            Invoice invoice = new Invoice(invoiceId, session.getSessionId(), session.getTotalCost());
            invoice.setStatus(status);
            invoicesById.put(invoiceId, invoice);
            sessionsByInvoiceId.put(invoiceId, session);
        }
    }

    @When("I request a list of all invoices")
    public void iRequestAListOfAllInvoices() {
        viewedInvoices = new ArrayList<>(invoicesById.values());
    }

    @Then("I should see {int} invoices in the list")
    public void iShouldSeeInvoicesInTheList(int expectedCount) {
        assertEquals(expectedCount, viewedInvoices.size());
    }

    @Then("the invoice {string} should show a total amount of {double}")
    public void theInvoiceShouldShowATotalAmountOf(String invoiceId, double expectedTotal) {
        Invoice invoice = invoicesById.get(invoiceId);
        assertNotNull(invoice);
        assertEquals(expectedTotal, invoice.getTotalCost());
    }

    @Then("the invoice {string} should be associated with {string}")
    public void theInvoiceShouldBeAssociatedWith(String invoiceId, String customerId) {
        Session session = sessionsByInvoiceId.get(invoiceId);
        assertNotNull(session);
        assertEquals(customerId, session.getCutomerId());
    }
}
