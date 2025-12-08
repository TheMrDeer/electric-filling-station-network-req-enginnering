Feature: Manage Balance
  As a Customer, I want to manage my prepaid credit to use charging services.

  Background:
    Given the E.Power system is initialized

  Scenario: Show available balance (Story #27)
    Given a customer "User123" exists with a balance of 25.00
    When I check the available balance for "User123"
    Then I should see a balance of 25.00

  Scenario: Payment to top up the balance (Story #29, #31)
    Given a customer "User123" exists with a balance of 10.00
    When I top up the balance by 50.00
    Then the payment process should be "Success"
    And the new balance for "User123" should be 60.00