package com.example.bank_o_app;

public class cash_out_data {
    public String cash_out_id, date_and_time;
    public double amount;

    public cash_out_data() {

    }

    public cash_out_data(String cash_out_id, String date_and_time, double amount) {
        this.cash_out_id = cash_out_id;
        this.date_and_time = date_and_time;
        this.amount = amount;
    }

    public String getDate_and_time() {
        return date_and_time;
    }

    public String getCash_out_id() {
        return cash_out_id;
    }

    public double getAmount() {
        return amount;
    }
}
