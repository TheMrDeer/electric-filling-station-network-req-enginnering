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

    @Given("the following invoices exist in the system:")
    public void theFollowingInvoicesExistInTheSystem(DataTable dataTable) {
        invoicesById.clear();
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
        viewedInvoices = new ArrayList<>(invoicesById.values());
    }

    @Then("I should see {int} invoices in the list")
    public void iShouldSeeInvoicesInTheList(int expectedCount) {
        assertEquals(expectedCount, viewedInvoices.size());
    }

    @Then("the invoice {string} should show a total amount of {double}")
    public void theInvoiceShouldShowATotalAmountOf(String invoiceId, double expectedTotal) {
        InvoiceRecord record = invoicesById.get(invoiceId);
        assertNotNull(record);
        assertEquals(expectedTotal, record.totalAmount());
    }

    @Then("the invoice {string} should be associated with {string}")
    public void theInvoiceShouldBeAssociatedWith(String invoiceId, String customerId) {
        InvoiceRecord record = invoicesById.get(invoiceId);
        assertNotNull(record);
        assertEquals(customerId, record.customerId());
    }

    private record InvoiceRecord(
            String invoiceId,
            String customerId,
            String location,
            double totalAmount,
            String status
    ) {
    }
}
