package org.example;

public class Customer extends User {

    private double accountBalance;

    public Customer (String userId, String name, String email, String password) {
        super(userId,name, email, password);
    }

    public double checkBalance() {
        return this.accountBalance;
    }

    //top up balance:
    public double rechargeAccount (double totalCost) { //totalCost = "amount"
        if (totalCost<=0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        accountBalance += totalCost;
        return this.checkBalance();
    }

    public double updateBalance(double totalCost) {
        if(totalCost < 0) {
            throw new IllegalArgumentException("Total costs must be >= 0");
        }
        if(totalCost > accountBalance) {
            throw new IllegalStateException("Not enough balance");
        }
        accountBalance -= totalCost;
        return this.checkBalance();
    }

    public String getCustomerInfo(){
        return getUserId() +" "+ this.accountBalance;
    }
}
