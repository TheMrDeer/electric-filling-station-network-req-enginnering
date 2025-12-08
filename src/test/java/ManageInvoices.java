import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ManageInvoices {
    @And("the following invoices exist in the system:")
    public void theFollowingInvoicesExistInTheSystem() {
    }

    @When("I request a list of all invoices")
    public void iRequestAListOfAllInvoices() {
    }

    @Then("I should see {int} invoices in the list")
    public void iShouldSeeInvoicesInTheList(int arg0) {
    }

    @And("the invoice {string} should show a total amount of {double}")
    public void theInvoiceShouldShowATotalAmountOf(String arg0, int arg1, int arg2) {
    }

    @And("the invoice {string} should be associated with {string}")
    public void theInvoiceShouldBeAssociatedWith(String arg0, String arg1) {
    }
}
