import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ReceiveInvoices {
    @Given("a finished charging session exists for customer {string}")
    public void aFinishedChargingSessionExistsForCustomer(String arg0) {
    }

    @And("the session details are:")
    public void theSessionDetailsAre() {
    }

    @When("I request the invoice for the last session")
    public void iRequestTheInvoiceForTheLastSession() {
    }

    @Then("a new invoice should be generated")
    public void aNewInvoiceShouldBeGenerated() {
    }

    @And("the invoice should contain the location {string}")
    public void theInvoiceShouldContainTheLocation(String arg0) {
    }

    @And("the invoice should list {string} duration")
    public void theInvoiceShouldListDuration(String arg0) {
    }

    @And("the final amount on the invoice should be {double}")
    public void theFinalAmountOnTheInvoiceShouldBe(int arg0, int arg1) {
    }
}
