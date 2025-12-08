package org.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class Charging {
    @And("a location {string} exists with the following stations:")
    public void aLocationExistsWithTheFollowingStations(String arg0) {
    }

    @When("I search for available charging stations")
    public void iSearchForAvailableChargingStations() {
    }

    @Then("I should see {string} with type {string} and state {string}")
    public void iShouldSeeWithTypeAndState(String arg0, String arg1, String arg2) {
    }

    @And("I should see {string} with type {string} and price {double}")
    public void iShouldSeeWithTypeAndPrice(String arg0, String arg1, int arg2, int arg3) {
    }

    @And("I should see {string} with state {string}")
    public void iShouldSeeWithState(String arg0, String arg1) {
    }

    @Given("I am at station {string}")
    public void iAmAtStation(String arg0) {
    }

    @And("the station {string} is {string}")
    public void theStationIs(String arg0, String arg1) {
    }

    @When("I start a charging session with energy type {string}")
    public void iStartAChargingSessionWithEnergyType(String arg0) {
    }

    @Then("the session should start successfully")
    public void theSessionShouldStartSuccessfully() {
    }

    @And("the station {string} state should change to {string}")
    public void theStationStateShouldChangeTo(String arg0, String arg1) {
    }

    @Given("I have an active charging session at {string}")
    public void iHaveAnActiveChargingSessionAt(String arg0) {
    }

    @And("the session has been running for {int} minutes")
    public void theSessionHasBeenRunningForMinutes(int arg0) {
    }

    @When("I end the charging session")
    public void iEndTheChargingSession() {
    }

    @And("the cost of {double} should be deducted from my balance")
    public void theCostOfShouldBeDeductedFromMyBalance(int arg0, int arg1) {
    }

    @And("my new balance should be {double}")
    public void myNewBalanceShouldBe(int arg0, int arg1) {
    }
}
