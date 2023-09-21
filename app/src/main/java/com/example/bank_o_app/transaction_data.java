package com.example.bank_o_app;

public class transaction_data {
    String email;
    double amount;

    public transaction_data(String email, double amount) {
        this.email = email;
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public double getAmount() {
        return amount;
    }
}
