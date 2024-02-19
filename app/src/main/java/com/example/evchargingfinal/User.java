package com.example.evchargingfinal;

import java.io.Serializable;

public class User implements Serializable {
    String user_id, user_name, user_email, user_mobile_number;
    int energy_used, points;

    public User() {
    }

    public User(String user_id, String user_name, String user_email, String user_mobile_number, int energy_used, int points) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_mobile_number = user_mobile_number;
        this.energy_used = energy_used;
        this.points = points;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_mobile_number() {
        return user_mobile_number;
    }

    public void setUser_mobile_number(String user_mobile_number) {
        this.user_mobile_number = user_mobile_number;
    }

    public int getEnergy_used() {
        return energy_used;
    }

    public void setEnergy_used(int energy_used) {
        this.energy_used = energy_used;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}