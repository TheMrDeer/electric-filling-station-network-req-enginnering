package org.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

public class UserAuthentication {
    @When("I register a new account with Customer Identity {string}")
    public void iRegisterANewAccountWithCustomerIdentity(String arg0) {


        Customer c1 = new Customer(arg0,"","","");
        c1.register();


    }

    @Then("a Customer Account should be created for {string}")
    public void aCustomerAccountShouldBeCreatedFor(String arg0) {
    }

    @And("the initial Balance should be {double}")
    public void theInitialBalanceShouldBe(int arg0, int arg1) {
    }

    @Given("a customer {string} exists")
    public void aCustomerExists(String arg0) {
    }

    @When("I delete the account for {string}")
    public void iDeleteTheAccountFor(String arg0) {
    }

    @Then("the customer {string} should no longer be in the system")
    public void theCustomerShouldNoLongerBeInTheSystem(String arg0) {
    }
}
