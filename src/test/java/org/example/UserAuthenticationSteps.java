package org.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserAuthenticationSteps {

    private Customer customer;
    @When("I register a new account with Customer Identity {string}")
    public void iRegisterANewAccountWithCustomerIdentity(String arg0) {
        customer = new Customer(arg0,"","","");
    }

    @Then("a Customer Account should be created for {string}")
    public void aCustomerAccountShouldBeCreatedFor(String arg0) {
        assertEquals(arg0,UserManager.getCustomerById(arg0).getUserId());
    }

    @And("the initial Balance of {string} should be {double}")
    public void theInitialBalanceOfShouldBe(String arg0, double arg1) {
        assertEquals(arg1,UserManager.getCustomerById(arg0).checkBalance());
    }

    @Given("a customer {string} exists")
    public void aCustomerExists(String arg0) {
        customer = new Customer(arg0,"","","");
    }

    @When("I delete the account for {string}")
    public void iDeleteTheAccountFor(String arg0) {
        UserManager.removeUser(UserManager.getCustomerById(arg0));
    }

    @Then("the customer {string} should no longer be in the system")
    public void theCustomerShouldNoLongerBeInTheSystem(String arg0) {
        assertNull(UserManager.getCustomerById(arg0));
    }

}
