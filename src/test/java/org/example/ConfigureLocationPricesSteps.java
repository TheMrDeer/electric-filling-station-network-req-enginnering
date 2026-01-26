package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigureLocationPricesSteps {

    private String lastErrorMessage;

    @When("{string} has the following prices active from {string}:")
    public void hasTheFollowingPricesActiveFrom(String locationName, String dateString, DataTable dataTable) {
        Location location = StationManager.getInstance().findLocationByName(locationName);
        Assertions.assertNotNull(location, "Location not found: " + locationName);

        LocalDateTime validFrom = LocalDateTime.parse(dateString);
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : rows) {
            String typeStr = row.get("Type");
            double pricePerMin = Double.parseDouble(row.get("PricePerMin"));
            double pricePerKwh = Double.parseDouble(row.get("PricePerKwh"));
            ChargingStationType type = ChargingStationType.valueOf(typeStr);

            Price price = new Price(location.getLocationId(), type, pricePerMin, pricePerKwh, validFrom, null);
            location.addPrice(price);
        }
    }

    @Then("the price for {string} at {string} on {string} should be {double} per minute and {double} per kWh")
    public void thePriceForAtOnShouldBePerMinuteAndPerKWh(String typeStr, String locationName, String dateString, double expectedMin, double expectedKwh) {
        Location location = StationManager.getInstance().findLocationByName(locationName);
        Assertions.assertNotNull(location, "Location not found: " + locationName);

        LocalDateTime checkTime = LocalDateTime.parse(dateString);
        ChargingStationType type = ChargingStationType.valueOf(typeStr);

        Price price = location.getPriceFor(type, checkTime);
        Assertions.assertNotNull(price, "No price found for " + typeStr + " at " + locationName + " on " + dateString);

        Assertions.assertEquals(expectedMin, price.getPricePerMinute(), 0.001);
        Assertions.assertEquals(expectedKwh, price.getPricePerKwh(), 0.001);
        System.out.println("Price verified for " + typeStr + " at " + locationName);
    }

    @Given("{string} has the following prices:")
    public void hasTheFollowingPrices(String locationName, DataTable table) {
        Location location = StationManager.getInstance().findLocationByName(locationName);
        Assertions.assertNotNull(location, "Location not found: " + locationName);

        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            String typeStr = row.get("Type");
            double priceVal = Double.parseDouble(row.get("Price"));
            ChargingStationType type = ChargingStationType.valueOf(typeStr);

            // Default to valid from a long time ago so it's active now
            Price price = new Price(location.getLocationId(), type, priceVal, 0.0, LocalDateTime.MIN, null);
            location.addPrice(price);
        }
    }

    @When("I try to set the price for {string} at {string} to {double}")
    public void iTryToSetThePriceForAtTo(String typeStr, String locationName, double priceVal) {
        try {
            Location location = StationManager.getInstance().findLocationByName(locationName);
            ChargingStationType type = ChargingStationType.valueOf(typeStr);
            // Assuming we are setting price per minute for simplicity, or both
            new Price(location.getLocationId(), type, priceVal, 0.0, LocalDateTime.now(), null);
        } catch (Exception e) {
            lastErrorMessage = e.getMessage();
        }
    }

    @Then("I should receive a configuration error message {string}")
    public void iShouldReceiveAConfigurationErrorMessage(String expectedMessage) {
        assertEquals(expectedMessage, lastErrorMessage);
    }
}
