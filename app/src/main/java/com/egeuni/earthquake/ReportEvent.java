package com.egeuni.earthquake;

public class ReportEvent {
    private int index;
    private String place;


    public ReportEvent(int i, String place) {
        this.index = i;
        this.place = place;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

}
