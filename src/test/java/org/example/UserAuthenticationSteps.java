package org.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

public class UserAuthenticationSteps {

    private Customer customer;
    private String lastErrorMessage;
    private String errorMsg;

    @When("I register a new account with Customer Identity {string}")
    public void iRegisterANewAccountWithCustomerIdentity(String arg0) {
        // Creating a new customer with default email and password for testing basic registration.
        customer = new Customer(arg0, arg0, arg0 + "@example.com", "password");
        customer.addUser();
    }

    @When("I register a new account with Customer Identity {string} and Email {string} and Password {string}")
    public void iRegisterANewAccountWithCustomerIdentityAndEmailAndPassword(String arg0, String arg1, String arg2) {
        try {
            // Attempting to create a customer with specific credentials to test validation logic.
            // Catching Exception to verify error handling in subsequent steps.
            customer = new Customer(arg0, "person123", arg1, arg2);
            customer.addUser();
        } catch (Exception e) {
            errorMsg = e.getMessage();
        }
    }

    @Then("a Customer Account should be created for {string}")
    public void aCustomerAccountShouldBeCreatedFor(String arg0) {
        // Verifying that the user was successfully added to the UserManager.
        assertNotNull(UserManager.getCustomerById(arg0));
        System.out.println("Customer account created: " + arg0);
    }

    @And("the initial Balance of {string} should be {double}")
    public void theInitialBalanceOfShouldBe(String arg0, double arg1) {
        // Checking that the initial balance is correctly set (usually 0.0) for a new user.
        assertEquals(arg1, UserManager.getCustomerById(arg0).checkBalance());
    }

    @Given("a customer {string} exists with Email {string} and Password {string}")
    public void aCustomerExistsWithEmailAndPassword(String arg0, String arg1, String arg2) {
        // Pre-creating a customer to set up the state for login or other user-specific tests.
        customer = new Customer(arg0, "person123", arg1, arg2);
        UserManager.addUser(customer);
    }

    @When("I delete the account for {string}")
    public void iDeleteTheAccountFor(String arg0) {
        // Removing the user to test account deletion functionality.
        UserManager.removeUser(UserManager.getCustomerById(arg0));
    }

    @Then("the customer {string} should no longer be in the system")
    public void theCustomerShouldNoLongerBeInTheSystem(String arg0) {
        // Confirming that the user is no longer retrievable from the UserManager.
        assertNull(UserManager.getCustomerById(arg0));
    }


    @Then("an error indicates that that the email is not valid")
    public void anErrorIndicatesThatThatTheEmailIsNotValid() {
        // Verifying that the specific error message for invalid email was thrown.
        assertEquals("Email is not valid", errorMsg);

    }

    @Then("an error indicates that the password must have at least {int} characters")
    public void anErrorIndicatesThatThePasswordMustHaveAtLeastCharacters(int arg0) {
        // Verifying that the specific error message for short password was thrown.
        // Using dynamic string construction to match the expected error message format.
        assertEquals("Password must have at least " + arg0 + " characters", errorMsg);
    }

    @When("I attempt to register a new account with Customer Identity {string}")
    public void iAttemptToRegisterANewAccountWithCustomerIdentity(String userId) {
        try {
            // Attempting registration that is expected to fail (e.g., duplicate user).
            customer = new Customer(userId, userId, userId + "@example.com", "password");
            customer.addUser();
        } catch (IllegalStateException e) {
            // Capturing the exception message to assert against it in the Then step.
            lastErrorMessage = e.getMessage();
        }
    }

    @Then("I should receive a registration error message {string}")
    public void iShouldReceiveARegistrationErrorMessage(String message) {
        // Asserting that the captured error message matches the expected output.
        assertEquals(message, lastErrorMessage);
    }

    @Given("a customer {string} exists")
    public void aCustomerExists(String customerId) {
        // Helper step to ensure a customer exists for scenarios requiring a valid user.
        customer = new Customer(customerId, customerId, customerId + "@example.com", "password");
        UserManager.addUser(customer);
    }
}
