package com.example.evchargingfinal;

import com.google.firebase.firestore.GeoPoint;

public class History {
    String history_id, history_time;
    int history_energy, history_price;
    GeoPoint history_location;

    public History() {
    }

    public History(String history_id, String history_time, int history_energy, int history_price, GeoPoint history_location) {
        this.history_id = history_id;
        this.history_time = history_time;
        this.history_energy = history_energy;
        this.history_price = history_price;
        this.history_location = history_location;
    }

    public String getHistory_id() {
        return history_id;
    }

    public void setHistory_id(String history_id) {
        this.history_id = history_id;
    }

    public String getHistory_time() {
        return history_time;
    }

    public void setHistory_time(String history_time) {
        this.history_time = history_time;
    }

    public int getHistory_energy() {
        return history_energy;
    }

    public void setHistory_energy(int history_energy) {
        this.history_energy = history_energy;
    }

    public int getHistory_price() {
        return history_price;
    }

    public void setHistory_price(int history_price) {
        this.history_price = history_price;
    }

    public GeoPoint getHistory_location() {
        return history_location;
    }

    public void setHistory_location(GeoPoint history_location) {
        this.history_location = history_location;
    }
}