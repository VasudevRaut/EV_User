package com.example.evchargingfinal;

import java.io.Serializable;

public class Rating implements Serializable {
    String rat_id, owner_id, owner_email, user_id, rat_name, rat_date, rat_title, rat_desc;
    double rat_rating;

    public Rating() {
    }

    public Rating(String rat_id, String owner_id, String owner_email, String user_id, String rat_name, String rat_date, String rat_title, String rat_desc, double rat_rating) {
        this.rat_id = rat_id;
        this.owner_id = owner_id;
        this.owner_email = owner_email;
        this.user_id = user_id;
        this.rat_name = rat_name;
        this.rat_date = rat_date;
        this.rat_title = rat_title;
        this.rat_desc = rat_desc;
        this.rat_rating = rat_rating;
    }

    public String getRat_id() {
        return rat_id;
    }

    public void setRat_id(String rat_id) {
        this.rat_id = rat_id;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getOwner_email() {
        return owner_email;
    }

    public void setOwner_email(String owner_email) {
        this.owner_email = owner_email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRat_name() {
        return rat_name;
    }

    public void setRat_name(String rat_name) {
        this.rat_name = rat_name;
    }

    public String getRat_date() {
        return rat_date;
    }

    public void setRat_date(String rat_date) {
        this.rat_date = rat_date;
    }

    public String getRat_title() {
        return rat_title;
    }

    public void setRat_title(String rat_title) {
        this.rat_title = rat_title;
    }

    public String getRat_desc() {
        return rat_desc;
    }

    public void setRat_desc(String rat_desc) {
        this.rat_desc = rat_desc;
    }

    public double getRat_rating() {
        return rat_rating;
    }

    public void setRat_rating(double rat_rating) {
        this.rat_rating = rat_rating;
    }
}
