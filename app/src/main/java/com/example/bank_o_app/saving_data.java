package com.example.bank_o_app;

public class saving_data {
    public String saving_id;
    public String saving_title;
    public double saving_amount;

    public saving_data() {

    }

    public saving_data(String saving_id, String saving_title, double saving_amount) {
        this.saving_id = saving_id;
        this.saving_title = saving_title;
        this.saving_amount = saving_amount;
    }

    public String getSaving_id() {
        return saving_id;
    }

    public String getSaving_title() {
        return saving_title;
    }

    public double getSaving_amount() {
        return saving_amount;
    }
}
