package com.egeuni.earthquake;

import android.content.Context;

public class Formatter {
    private String date;

    public Formatter() {
        date = null;
    }

    public String getDateFormat(long timestamp) {
        java.util.Date d = new java.util.Date(timestamp);
        date = d.toString();
     return extractTimeInfo(date);
    }

    public String getPlaceFormat(String place) {
        return extractDigits(place) + " km" +"\n" + extractPlace(place);
    }

    public String getMagFeltFormat(Double value) {
        if(value == 0.0) {
            return "-";
        } else {
            return value +"";
        }
    }

    private String extractTimeInfo(String string) {
        String[] strings = string.split(" GMT");

        return strings[0].substring(0,10) + "\n" + strings[0].substring(11,strings[0].length());
    }

    private String extractDigits(String string) {
        String str = null;
        str = string.replaceAll("\\D+","");

        return str;
    }

    private String extractPlace(String string) {
      String[] strings = string.split("of ");

      return strings[1];
    }

}
