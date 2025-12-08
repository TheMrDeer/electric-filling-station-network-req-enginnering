package org.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ConfigureLocationPrices {
    @When("I set the price for {string} charging at {string} to {double} per minute")
    public void iSetThePriceForChargingAtToPerMinute(String arg0, String arg1, int arg2, int arg3) {
    }

    @Then("the price configuration for {string} at {string} should be {double}")
    public void thePriceConfigurationForAtShouldBe(String arg0, String arg1, int arg2, int arg3) {
    }

    @Given("{string} has the following prices:")
    public void hasTheFollowingPrices(String arg0) {
    }

    @When("I view prices for {string}")
    public void iViewPricesFor(String arg0) {
    }

    @Then("I should see {double} for {string} and {double} for {string}")
    public void iShouldSeeForAndFor(int arg0, int arg1, String arg2, int arg3, int arg4, String arg5) {
    }
}
