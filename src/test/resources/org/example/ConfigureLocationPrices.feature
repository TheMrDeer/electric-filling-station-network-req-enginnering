Feature: Configure Location Prices
  As the Owner, I want to manage pricing configurations with temporal validity.

  Scenario: Set price per minute and kWh of charging per location and charging type with start date
    Given a location named "Vienna Central" exists
    When "Vienna Central" has the following prices active from "2023-01-01T00:00":
      | Type | PricePerMin | PricePerKwh |
      | AC   | 0.10        | 0.35        |
      | DC   | 0.20        | 0.60        |
    Then the price for "AC" at "Vienna Central" on "2023-01-15T12:00" should be 0.10 per minute and 0.35 per kWh

  Scenario: Verify temporal validity of prices
    Given a location named "Vienna Central" exists
    And "Vienna Central" has the following prices active from "2023-01-01T00:00":
      | Type | PricePerMin | PricePerKwh |
      | AC   | 0.10        | 0.35        |
    And "Vienna Central" has the following prices active from "2023-02-01T00:00":
      | Type | PricePerMin | PricePerKwh |
      | AC   | 0.15        | 0.40        |
    Then the price for "AC" at "Vienna Central" on "2023-01-15T12:00" should be 0.10 per minute and 0.35 per kWh
    And the price for "AC" at "Vienna Central" on "2023-02-02T12:00" should be 0.15 per minute and 0.40 per kWh

  Scenario: Prevent setting negative prices (Error Case)
    Given a location named "Vienna Central" exists
    When I try to set the price for "AC" at "Vienna Central" to -0.50
    Then I should receive a configuration error message "Price cannot be negative"
