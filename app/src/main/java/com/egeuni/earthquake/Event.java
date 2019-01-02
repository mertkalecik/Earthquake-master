package com.egeuni.earthquake;

import android.widget.TextView;

import java.io.Serializable;

public class Event implements Serializable {

    /** Title of the earthquake event */
    private  String place;
    private  String date;
    private  String hour;
    private  String mag;
    private  String depth;
    private  String latitude;
    private  String longitude;
    private int hDepth;
    private int hMag;


    public Event(String place, String date, String hour, String mag, String depth, String latitude, String longitude, int hDepth, int hMag) {
        this.place =place;
        this.date = date;
        this.hour = hour;
        this.mag = mag;
        this.depth = depth;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hDepth = hDepth;
        this.hMag = hMag;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getMag() {
        return mag;
    }

    public void setMag(String mag) {this.mag = mag;}

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setHour(String h) {
        this.hour = h;
    }

    public String getHour() {
        return hour;
    }

    public void setLatitude(String l) {
        this.latitude = l;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public int gethDepth() {
        return hDepth;
    }

    public void sethDepth(int hDepth) {
        this.hDepth = hDepth;
    }

    public int gethMag() {
        return hMag;
    }

    public void sethMag(int hMag) {
        this.hMag = hMag;
    }
}