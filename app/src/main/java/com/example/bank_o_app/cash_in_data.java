package com.example.bank_o_app;

public class cash_in_data {
    public String cash_in_id, date_and_time;
    public double amount;

    public cash_in_data() {

    }

    public cash_in_data(String cash_in_id, String date_and_time, double amount) {
        this.cash_in_id = cash_in_id;
        this.date_and_time = date_and_time;
        this.amount = amount;
    }

    public String getDate_and_time() {
        return date_and_time;
    }

    public String getCash_in_id() {
        return cash_in_id;
    }

    public double getAmount() {
        return amount;
    }
}
