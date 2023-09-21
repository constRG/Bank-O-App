package com.example.bank_o_app;
public class user_data {
    public double available_balance;
    public String fullname, email, address, mobile_no;

    public user_data() {

    }
    public user_data( double available_balance, String fullname, String email, String address, String mobile_no) {
        this.available_balance = available_balance;
        this.fullname = fullname;
        this.email = email;
        this.address = address;
        this.mobile_no = mobile_no;
    }

    public double getAvailable_balance() {
        return available_balance;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getMobile_no() {
        return mobile_no;
    }


}
