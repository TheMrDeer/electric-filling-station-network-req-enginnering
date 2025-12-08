package org.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ManageChargingStation {
    @Given("a location named {string} exists")
    public void aLocationNamedExists(String arg0) {
    }

    @When("I add a charging station with ID {string} to {string}")
    public void iAddAChargingStationWithIDTo(String arg0, String arg1) {
    }

    @Then("the station {string} should be associated with {string}")
    public void theStationShouldBeAssociatedWith(String arg0, String arg1) {
    }

    @Given("a station {string} exists at {string}")
    public void aStationExistsAt(String arg0, String arg1) {
    }

    @When("I set the charging type of {string} to {string}")
    public void iSetTheChargingTypeOfTo(String arg0, String arg1) {
    }

    @Then("the station {string} should be identified as a {string} station")
    public void theStationShouldBeIdentifiedAsAStation(String arg0, String arg1) {
    }

    @Given("a station {string} exists")
    public void aStationExists(String arg0) {
    }

    @When("I set the state of {string} to {string}")
    public void iSetTheStateOfTo(String arg0, String arg1) {
    }

    @Then("the station {string} status should be {string}")
    public void theStationStatusShouldBe(String arg0, String arg1) {
    }

    @Given("the following stations exist at {string}:")
    public void theFollowingStationsExistAt(String arg0) {
    }

    @When("I request a list of {string} stations that are {string}")
    public void iRequestAListOfStationsThatAre(String arg0, String arg1) {
    }

    @Then("I should receive only station {string}")
    public void iShouldReceiveOnlyStation(String arg0) {
    }
}
