package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


import java.util.HashMap;
import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigureLocationPrices {

    private final Map<String, Double> pricesByLocationAndType = new HashMap<>();

    private Map<String, Double> viewedPrices = new HashMap<>();

    private String key(String locationName, String type) {
        return locationName + "|" + type.trim().toUpperCase();
    }
    @Given("a location named {string} exists")
    public void aLocationNamedExists(String arg0) {
        pricesByLocationAndType.clear();
        viewedPrices.clear();
    }

    @When("I set the price for {string} charging at {string} to {double} per minute")
    public void iSetThePriceForChargingAtToPerMinute(String type, String locationName, double ratePerMinute) {
        pricesByLocationAndType.put(key(locationName, type), ratePerMinute);
    }

    @Then("the price configuration for {string} at {string} should be {double}")
    public void thePriceConfigurationForAtShouldBe(String type, String locationName, double ratePerMinute) {
        Double actual = pricesByLocationAndType.get(key(locationName, type));
        assertEquals(ratePerMinute, actual);
    }

    @Given("{string} has the following prices:")
    public void hasTheFollowingPrices(String locationName, DataTable table) {
        pricesByLocationAndType.clear();
        viewedPrices.clear();

        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            String type = row.get("Type");
            double price = Double.parseDouble(row.get("Price"));
            pricesByLocationAndType.put(key(locationName, type), price);
        }
    }

    @When("I view prices for {string}")
    public void iViewPricesFor(String locationName) {
        viewedPrices = new HashMap<>();
        for (var entry : pricesByLocationAndType.entrySet()) {
            String k = entry.getKey();
            if (k.startsWith(locationName + "|")) {
                String type = k.substring((locationName + "|").length());
                viewedPrices.put(type, entry.getValue());
            }
        }
    }

    @Then("I should see {double} for {string} and {double} for {string}")
    public void iShouldSeeForAndFor(double price1, String type1, double price2, String type2) {
        assertEquals(price1, viewedPrices.get(type1.trim().toUpperCase()));
        assertEquals(price2, viewedPrices.get(type2.trim().toUpperCase()));
    }
}
