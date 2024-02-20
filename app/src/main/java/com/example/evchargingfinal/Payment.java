package com.example.evchargingfinal;

public class Payment {
    String payment_id, payment_from_user_id, payment_from_name, payment_to_owner_id, payment_to_name, payment_date;
    int payment_amount, payment_energy_sold;

    public Payment() {
    }


    public Payment(String payment_id, String payment_from_user_id, String payment_from_name, String payment_to_owner_id, String payment_to_name, String payment_date, int payment_amount, int payment_energy_sold) {
        this.payment_id = payment_id;
        this.payment_from_user_id = payment_from_user_id;
        this.payment_from_name = payment_from_name;
        this.payment_to_owner_id = payment_to_owner_id;
        this.payment_to_name = payment_to_name;
        this.payment_date = payment_date;
        this.payment_amount = payment_amount;
        this.payment_energy_sold = payment_energy_sold;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getPayment_from_user_id() {
        return payment_from_user_id;
    }

    public void setPayment_from_user_id(String payment_from_user_id) {
        this.payment_from_user_id = payment_from_user_id;
    }

    public String getPayment_from_name() {
        return payment_from_name;
    }

    public void setPayment_from_name(String payment_from_name) {
        this.payment_from_name = payment_from_name;
    }

    public String getPayment_to_owner_id() {
        return payment_to_owner_id;
    }

    public void setPayment_to_owner_id(String payment_to_owner_id) {
        this.payment_to_owner_id = payment_to_owner_id;
    }

    public String getPayment_to_name() {
        return payment_to_name;
    }

    public void setPayment_to_name(String payment_to_name) {
        this.payment_to_name = payment_to_name;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public int getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(int payment_amount) {
        this.payment_amount = payment_amount;
    }

    public int getPayment_energy_sold() {
        return payment_energy_sold;
    }

    public void setPayment_energy_sold(int payment_energy_sold) {
        this.payment_energy_sold = payment_energy_sold;
    }
}