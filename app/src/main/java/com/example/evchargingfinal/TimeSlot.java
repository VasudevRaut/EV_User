package com.example.evchargingfinal;

public class TimeSlot {

    String chargeStationN,time,User_mail,status;

    public TimeSlot(String chargeStationN, String time, String user_mail,String status) {
        this.chargeStationN = chargeStationN;
        this.time = time;
        User_mail = user_mail;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChargeStationN() {
        return chargeStationN;
    }

    public void setChargeStationN(String chargeStationN) {
        this.chargeStationN = chargeStationN;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser_mail() {
        return User_mail;
    }

    public void setUser_mail(String user_mail) {
        User_mail = user_mail;
    }
}
