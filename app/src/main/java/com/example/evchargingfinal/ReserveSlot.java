package com.example.evchargingfinal;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class ReserveSlot implements Serializable {
    String rs_id, owner_id, user_id, evs_id, owner_email, user_email;
    Timestamp rs_start, rs_end;

    public ReserveSlot() {
    }

    public ReserveSlot(String rs_id, String owner_id, String user_id, String evs_id, String owner_email, String user_email, Timestamp rs_start, Timestamp rs_end) {
        this.rs_id = rs_id;
        this.owner_id = owner_id;
        this.user_id = user_id;
        this.evs_id = evs_id;
        this.owner_email = owner_email;
        this.user_email = user_email;
        this.rs_start = rs_start;
        this.rs_end = rs_end;
    }

    public String getRs_id() {
        return rs_id;
    }

    public void setRs_id(String rs_id) {
        this.rs_id = rs_id;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEvs_id() {
        return evs_id;
    }

    public void setEvs_id(String evs_id) {
        this.evs_id = evs_id;
    }

    public String getOwner_email() {
        return owner_email;
    }

    public void setOwner_email(String owner_email) {
        this.owner_email = owner_email;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public Timestamp getRs_start() {
        return rs_start;
    }

    public void setRs_start(Timestamp rs_start) {
        this.rs_start = rs_start;
    }

    public Timestamp getRs_end() {
        return rs_end;
    }

    public void setRs_end(Timestamp rs_end) {
        this.rs_end = rs_end;
    }
}
