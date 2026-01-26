package org.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ManageLocationSteps {

    private Location location;
    private Location location2;
    boolean isListed;
    private String lastError;

    @When("I add a new location named {string}")
    public void iAddANewLocationNamed(String locationName) {
        // Creating a new location object to test the addition functionality.
        location = new Location("LOC-1",locationName,"",Status.Active);
    }

    @Then("the location {string} should be listed in the Charging Network")
    public void theLocationShouldBeListedInTheChargingNetwork(String locationName) {
        // Iterating through all locations to verify the new location is present.
        for(Location l:StationManager.getInstance().getLocations()) {
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
        // Creating a location instance for test setup (variable name location2 implies secondary or specific use).
        location2 = new Location("LOC-2",locationName,"",Status.Active);
    }

    @Given("a location named {string} exists in the network")
    public void aLocationNamedExistsInTheNetwork(String locationName) {
        // Similar to above, but explicitly for "in the network" context, using a different ID.
        location2 = new Location("LOC-3",locationName,"",Status.Active);
    }

    @When("I remove the location {string}")
    public void iRemoveTheLocation(String locationName) {
        // Finding and removing the location to test deletion logic.
        StationManager.getInstance().removeLocation(StationManager.getInstance().findLocationByName(locationName));
    }

    @Then("the location {string} should no longer exist in the network")
    public void theLocationShouldNoLongerExistInTheNetwork(String arg0) {
        // Verifying that the location cannot be found after removal.
        assertNull(StationManager.getInstance().findLocationByName(arg0));
    }

    @When("I attempt to remove the location {string}")
    public void iAttemptToRemoveTheLocation(String locationName) {
        try {
            // Attempting removal of a potentially non-existent location to test error handling.
            StationManager.getInstance().removeLocation(StationManager.getInstance().findLocationByName(locationName));
        } catch (IllegalStateException e) {
            // Capturing the exception message for validation.
            lastError = e.getMessage();
        }
    }

    @Then("I should receive a location error message {string}")
    public void iShouldReceiveALocationErrorMessage(String expectedMessage) {
        // Asserting the error message matches the expected output.
        assertEquals(expectedMessage, lastError);
    }
}
