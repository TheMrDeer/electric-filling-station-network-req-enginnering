import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ManageLocation {
    @Given("the E.Power system is initialized")
    public void theEPowerSystemIsInitialized() {
    }

    @When("I add a new location named {string}")
    public void iAddANewLocationNamed(String arg0) {
    }

    @Then("the location {string} should be listed in the Charging Network")
    public void theLocationShouldBeListedInTheChargingNetwork(String arg0) {
    }

    @Given("a location named {string} exists in the network")
    public void aLocationNamedExistsInTheNetwork(String arg0) {
    }

    @When("I remove the location {string}")
    public void iRemoveTheLocation(String arg0) {
    }

    @Then("the location {string} should no longer exist in the network")
    public void theLocationShouldNoLongerExistInTheNetwork(String arg0) {
    }
}
