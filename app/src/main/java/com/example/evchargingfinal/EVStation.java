package com.example.evchargingfinal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EVStation {
    String evs_id;
    int evs_available, evs_energy, type;
    //    int[] slot = new int[48];
    List<Integer> slot = new ArrayList<>(Arrays.asList(new Integer[48]));

    public EVStation() {
    }

    public EVStation(List<Integer> slot) {
        this.slot = slot;
}

    public EVStation(String evs_id, List<Integer> slot) {
        this.evs_id = evs_id;
        this.evs_available = 0;
        this.evs_energy = 0;
        this.type = 0;
        this.slot = slot;
    }

    public EVStation(String evs_id, int evs_available, int evs_energy, int type, List<Integer> slot) {
        this.evs_id = evs_id;
        this.evs_available = evs_available;
        this.evs_energy = evs_energy;
        this.type = type;
        this.slot = slot;
    }

    public String getEvs_id() {
        return evs_id;
    }

    public void setEvs_id(String evs_id) {
        this.evs_id = evs_id;
    }

    public int getEvs_available() {
        return evs_available;
    }

    public void setEvs_available(int evs_available) {
        this.evs_available = evs_available;
    }

    public int getEvs_energy() {
        return evs_energy;
    }

    public void setEvs_energy(int evs_energy) {
        this.evs_energy = evs_energy;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Integer> getSlot() {
        return slot;
    }

    public void setSlot(List<Integer> slot) {
        this.slot = slot;
    }
}