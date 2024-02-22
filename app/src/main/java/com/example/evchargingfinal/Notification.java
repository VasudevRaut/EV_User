package com.example.evchargingfinal;

public class Notification {
    String noti_email, noti_token;

    public Notification() {
    }

    public Notification(String noti_email, String noti_token) {
        this.noti_email = noti_email;
        this.noti_token = noti_token;
    }

    public String getNoti_email() {
        return noti_email;
    }

    public void setNoti_email(String noti_email) {
        this.noti_email = noti_email;
    }

    public String getNoti_token() {
        return noti_token;
    }

    public void setNoti_token(String noti_token) {
        this.noti_token = noti_token;
    }
}