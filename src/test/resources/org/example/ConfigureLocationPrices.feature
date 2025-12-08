Feature: Configure Location Prices
  As the Owner, I want to manage pricing configurations.

  Scenario: Set price per minute of charging per location and charging type (Story #13)
    Given a location named "Vienna Central" exists
    When I set the price for "DC" charging at "Vienna Central" to 0.50 per minute
    Then the price configuration for "DC" at "Vienna Central" should be 0.50

  Scenario: View current prices by location (Story #14)
    Given "Vienna Central" has the following prices:
      | Type | Price |
      | AC   | 0.30  |
      | DC   | 0.50  |
    When I view prices for "Vienna Central"
    Then I should see 0.30 for "AC" and 0.50 for "DC"