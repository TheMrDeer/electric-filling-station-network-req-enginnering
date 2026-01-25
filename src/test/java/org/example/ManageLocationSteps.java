package org.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ManageLocationSteps {

    private Location location;
    private Location location2;
    boolean isListed;
    @When("I add a new location named {string}")
    public void iAddANewLocationNamed(String locationName) {
        location = new Location("LOC-1",locationName,"",Status.Active);
    }

    @Then("the location {string} should be listed in the Charging Network")
    public void theLocationShouldBeListedInTheChargingNetwork(String locationName) {
        for(Location l:StationManager.getLocations()) {
            if (l.getName().equals(locationName)) {
                isListed = true;
                break;
            }
        }
        assertTrue(isListed);
        System.out.println("Location listed: " + locationName);
    }

    @Given("a location named {string} exists")
    public void aLocationNamedExists(String locationName) {
        location2 = new Location("LOC-2",locationName,"",Status.Active);
    }

    @Given("a location named {string} exists in the network")
    public void aLocationNamedExistsInTheNetwork(String locationName) {
        location2 = new Location("LOC-3",locationName,"",Status.Active);
    }

    @When("I remove the location {string}")
    public void iRemoveTheLocation(String locationName) {
        StationManager.removeLocation(StationManager.findLocationByName(locationName));
    }

    @Then("the location {string} should no longer exist in the network")
    public void theLocationShouldNoLongerExistInTheNetwork(String arg0) {
        assertNull(StationManager.findLocationByName(arg0));
    }
}
