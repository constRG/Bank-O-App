package com.example.bank_o_app;

public class contact_data {
    public String contact_id, fullname, email, mobile_no;

    public contact_data() {

    }

    public contact_data(String contact_id, String fullname, String email, String mobile_no) {
        this.contact_id = contact_id;
        this.fullname = fullname;
        this.email = email;
        this.mobile_no = mobile_no;
    }

    public String getContact_id() {
        return contact_id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile_no() {
        return mobile_no;
    }

}
