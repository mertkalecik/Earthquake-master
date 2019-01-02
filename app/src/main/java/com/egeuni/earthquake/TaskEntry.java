package com.egeuni.earthquake;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName="task")
public class TaskEntry {

    /** Title of the earthquake event */
    @PrimaryKey(autoGenerate = true)
    private  int id;
    private int hazardDepth;
    private int hazardMag;
    private  String place;
    private  String date;
    private  String hour;
    private  String mag;
    private  String depth;
    private  String latitude;
    private  String longitude;

    @Ignore
    public TaskEntry(String place, String date, String hour, String mag, String depth, String latitude, String longitude, int hazardDepth, int hazardMag) {
        this.place =place;
        this.date = date;
        this.hour = hour;
        this.mag = mag;
        this.depth = depth;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hazardDepth = hazardDepth;
        this.hazardMag = hazardMag;

    }

    public TaskEntry(int id, String place, String date, String hour, String mag, String depth, String latitude, String longitude, int hazardDepth, int hazardMag) {
        this.id = id;
        this.place =place;
        this.date = date;
        this.hour = hour;
        this.mag = mag;
        this.depth = depth;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hazardDepth = hazardDepth;
        this.hazardMag = hazardMag;
    }


    public int getId() {
        return id;
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

    public int getHazardDepth() {
        return hazardDepth;
    }

    public int getHazardMag() {
        return hazardMag;
    }
}