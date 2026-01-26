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
        // Use default credentials for basic registration test
        customer = new Customer(arg0, arg0, arg0 + "@example.com", "password");
        customer.addUser();
    }

    @When("I register a new account with Customer Identity {string} and Email {string} and Password {string}")
    public void iRegisterANewAccountWithCustomerIdentityAndEmailAndPassword(String arg0, String arg1, String arg2) {
        try {
            // Attempt creation with specific credentials to test validation
            customer = new Customer(arg0, "person123", arg1, arg2);
            customer.addUser();
        } catch (Exception e) {
            errorMsg = e.getMessage();
        }
    }

    @Then("a Customer Account should be created for {string}")
    public void aCustomerAccountShouldBeCreatedFor(String arg0) {
        // Verify user was added to UserManager
        assertNotNull(UserManager.getCustomerById(arg0));
        System.out.println("Customer account created: " + arg0);
    }

    @And("the initial Balance of {string} should be {double}")
    public void theInitialBalanceOfShouldBe(String arg0, double arg1) {
        // Check initial balance is correct
        assertEquals(arg1, UserManager.getCustomerById(arg0).checkBalance());
    }

    @Given("a customer {string} exists with Email {string} and Password {string}")
    public void aCustomerExistsWithEmailAndPassword(String arg0, String arg1, String arg2) {
        // Pre-create customer for login/user tests
        customer = new Customer(arg0, "person123", arg1, arg2);
        UserManager.addUser(customer);
    }

    @When("I delete the account for {string}")
    public void iDeleteTheAccountFor(String arg0) {
        // Remove user to test deletion
        UserManager.removeUser(UserManager.getCustomerById(arg0));
    }

    @Then("the customer {string} should no longer be in the system")
    public void theCustomerShouldNoLongerBeInTheSystem(String arg0) {
        // Confirm user is removed
        assertNull(UserManager.getCustomerById(arg0));
    }


    @Then("an error indicates that that the email is not valid")
    public void anErrorIndicatesThatThatTheEmailIsNotValid() {
        // Verify invalid email error
        assertEquals("Email is not valid", errorMsg);

    }

    @Then("an error indicates that the password must have at least {int} characters")
    public void anErrorIndicatesThatThePasswordMustHaveAtLeastCharacters(int arg0) {
        // Verify short password error
        assertEquals("Password must have at least " + arg0 + " characters", errorMsg);
    }

    @When("I attempt to register a new account with Customer Identity {string}")
    public void iAttemptToRegisterANewAccountWithCustomerIdentity(String userId) {
        try {
            // Attempt registration expected to fail
            customer = new Customer(userId, userId, userId + "@example.com", "password");
            customer.addUser();
        } catch (IllegalStateException e) {
            // Capture exception message
            lastErrorMessage = e.getMessage();
        }
    }

    @Then("I should receive a registration error message {string}")
    public void iShouldReceiveARegistrationErrorMessage(String message) {
        // Assert captured error matches expected
        assertEquals(message, lastErrorMessage);
    }

    @Given("a customer {string} exists")
    public void aCustomerExists(String customerId) {
        // Helper to ensure customer exists
        customer = new Customer(customerId, customerId, customerId + "@example.com", "password");
        UserManager.addUser(customer);
    }
}
