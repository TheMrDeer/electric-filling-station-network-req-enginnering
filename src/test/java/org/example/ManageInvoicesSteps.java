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

    private final Map<String, InvoiceRecord> invoicesById = new HashMap<>();
    private List<InvoiceRecord> viewedInvoices = new ArrayList<>();
    private String lastError;

    @Given("the following invoices exist in the system:")
    public void theFollowingInvoicesExistInTheSystem(DataTable dataTable) {
        invoicesById.clear();
        // Parsing the DataTable to populate the system with mock invoice records for testing.
        for (Map<String, String> row : dataTable.asMaps(String.class, String.class)) {
            InvoiceRecord record = new InvoiceRecord(
                    row.get("InvoiceID"),
                    row.get("CustomerID"),
                    row.get("Location"),
                    Double.parseDouble(row.get("TotalAmount")),
                    row.get("Status")
            );
            invoicesById.put(record.invoiceId(), record);
        }
    }

    @When("I request a list of all invoices")
    public void iRequestAListOfAllInvoices() {
        // Simulating the action of retrieving all invoices.
        viewedInvoices = new ArrayList<>(invoicesById.values());
    }

    @Then("I should see {int} invoices in the list")
    public void iShouldSeeInvoicesInTheList(int expectedCount) {
        // Verifying that the number of retrieved invoices matches the expected count.
        assertEquals(expectedCount, viewedInvoices.size());
        System.out.println("Found " + viewedInvoices.size() + " invoices.");
    }

    @Then("the invoice {string} should show a total amount of {double}")
    public void theInvoiceShouldShowATotalAmountOf(String invoiceId, double expectedTotal) {
        // Retrieving a specific invoice by ID to verify its total amount.
        InvoiceRecord record = invoicesById.get(invoiceId);
        assertNotNull(record);
        assertEquals(expectedTotal, record.totalAmount());
    }

    @Then("the invoice {string} should be associated with {string}")
    public void theInvoiceShouldBeAssociatedWith(String invoiceId, String customerId) {
        // Verifying that the invoice is linked to the correct customer.
        InvoiceRecord record = invoicesById.get(invoiceId);
        assertNotNull(record);
        assertEquals(customerId, record.customerId());
    }

    @When("I request an invoice for a non-existent customer {string}")
    public void iRequestAnInvoiceForANonExistentCustomer(String customerId) {
        try {
            // Simulating a request for a user that doesn't exist to test error handling.
            Customer customer = UserManager.getCustomerById(customerId);
            if (customer == null) {
                throw new IllegalArgumentException("Customer not found");
            }
            // Logic to get invoices for customer would go here
        } catch (IllegalArgumentException e) {
            // Capturing the exception message for verification.
            lastError = e.getMessage();
        }
    }

    @Then("I should receive an invoice error message {string}")
    public void iShouldReceiveAnInvoiceErrorMessage(String expectedMessage) {
        // Asserting that the correct error message was returned.
        assertEquals(expectedMessage, lastError);
    }

    // Using a Java Record to model immutable invoice data for the test context.
    private record InvoiceRecord(
            String invoiceId,
            String customerId,
            String location,
            double totalAmount,
            String status
    ) {
    }
}
