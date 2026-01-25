Feature: Receive Invoice
  As a Customer, I want to receive a transparent invoice after using the service.

  Scenario: Generate detailed invoice with multiple sessions and top-ups
    Given a customer "User123" exists with balance 0.00
    And the customer tops up 100.00
    And the customer has the following finished sessions:
      | Location       | Type | Date             | Duration | kWh  | Cost  |
      | Vienna Central | AC   | 2023-01-01T10:00 | 30       | 10.0 | 5.00  |
      | Graz Main      | DC   | 2023-01-02T14:00 | 15       | 20.0 | 15.00 |
    When I request the invoice
    Then the invoice should list 2 charging sessions sorted by date
    And the invoice should list the Top-Up of 100.00
    And the first entry should contain "Vienna Central", "AC", and "5.00"
    And the invoice should show the current remaining balance
