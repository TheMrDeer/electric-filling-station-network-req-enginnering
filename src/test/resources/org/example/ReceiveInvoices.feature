Feature: Receive Invoice
  As a Customer, I want to receive a transparent invoice after using the service.

  Scenario: Generate detailed invoice with multiple sessions and top-ups
    Given a customer "User123" exists with balance 0.00
    And the customer tops up 100.00
    And the customer has the following finished sessions:
      | Location       | Station ID | Type | Date             | Duration | kWh  | Cost  |
      | Vienna Central | CS-101     | AC   | 2023-01-01T10:00 | 30       | 10.0 | 5.00  |
      | Graz Main      | CS-102     | DC   | 2023-01-02T14:00 | 15       | 20.0 | 15.00 |
    When I request the invoice
    Then the invoice should list 2 charging sessions sorted by date
    And the invoice should list the Top-Up of 100.00
    And the first entry should contain "Vienna Central", "AC", and "5.00"
    And the invoice should contain the Invoice Item Number "1"
    And the invoice should contain the Charging Station ID "CS-101"
    And the invoice should have a unique Invoice ID
    And the invoice should show the current remaining balance
