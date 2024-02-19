package com.example.evchargingfinal;

import com.google.firebase.firestore.GeoPoint;

public class Owner1 {
    String owner_id, owner_email, owner_name, ev_station_name;
    double avg_rating;
    private GeoPoint owner_location;
    int charging_points, price, charging_point_com_type_1, charging_point_com_type_2, charging_point_com_type_3, reviews;

    public Owner1() {
    }

    public Owner1(String owner_id, String owner_email, String owner_name, String ev_station_name, double avg_rating, GeoPoint owner_location, int charging_points, int price, int charging_point_com_type_1, int charging_point_com_type_2, int charging_point_com_type_3, int reviews) {
        this.owner_id = owner_id;
        this.owner_email = owner_email;
        this.owner_name = owner_name;
        this.ev_station_name = ev_station_name;
        this.avg_rating = avg_rating;
        this.owner_location = owner_location;
        this.charging_points = charging_points;
        this.price = price;
        this.charging_point_com_type_1 = charging_point_com_type_1;
        this.charging_point_com_type_2 = charging_point_com_type_2;
        this.charging_point_com_type_3 = charging_point_com_type_3;
        this.reviews = reviews;
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

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getEv_station_name() {
        return ev_station_name;
    }

    public void setEv_station_name(String ev_station_name) {
        this.ev_station_name = ev_station_name;
    }

    public double getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(double avg_rating) {
        this.avg_rating = avg_rating;
    }

    public GeoPoint getOwner_location() {
        return owner_location;
    }

    public void setOwner_location(GeoPoint owner_location) {
        this.owner_location = owner_location;
    }

    public int getCharging_points() {
        return charging_points;
    }

    public void setCharging_points(int charging_points) {
        this.charging_points = charging_points;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCharging_point_com_type_1() {
        return charging_point_com_type_1;
    }

    public void setCharging_point_com_type_1(int charging_point_com_type_1) {
        this.charging_point_com_type_1 = charging_point_com_type_1;
    }

    public int getCharging_point_com_type_2() {
        return charging_point_com_type_2;
    }

    public void setCharging_point_com_type_2(int charging_point_com_type_2) {
        this.charging_point_com_type_2 = charging_point_com_type_2;
    }

    public int getCharging_point_com_type_3() {
        return charging_point_com_type_3;
    }

    public void setCharging_point_com_type_3(int charging_point_com_type_3) {
        this.charging_point_com_type_3 = charging_point_com_type_3;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }
}