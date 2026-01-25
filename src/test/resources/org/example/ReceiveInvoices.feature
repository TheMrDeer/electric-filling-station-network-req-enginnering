Feature: Receive Invoice
  As a Customer, I want to receive a transparent invoice after using the service.
# Noch einmal ansehen was in der Rechnung beeinhaltet ist
  Background:
    Given a finished charging session exists for customer "User123"
    And the session details are:
      | Station   | Vienna Central - CS-103 |
      | Duration  | 30 min                  |
      | Price     | 0.50 per min            |
      | Total     | 15.00                   |

  Scenario: Generate invoice (Story #51)
    When I request the invoice for the last session
    Then a new invoice should be generated
    And the invoice should contain the location "Vienna Central"
    And the invoice should list "30 min" duration
    And the final amount on the invoice should be 15.00
    And the charging station type is ..