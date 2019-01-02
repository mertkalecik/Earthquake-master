package com.egeuni.earthquake;


import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

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

    public String extractCity(String result){
        String str;
        if(!result.contains("(")) {
            str = "ROMA";
        } else {
            str = result.substring(result.indexOf("(")+1,result.indexOf(")"));
        }

        Log.d("FindMe", "extractCity: " + str);
        return str;
    }

    public int getColorId(Double mag) {

        if(mag <= 3.0) {
            return R.drawable.low_risky_button;
        } else if(mag <= 5.0) {
            return R.drawable.risky_button;
        } else {
            return R.drawable.high_risky_button;
        }
    }

    public boolean isNewEarthquake(String earthquakeString) {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        // remove next line if you're always using the current time.
        cal.setTime(now);
        cal.add(Calendar.HOUR,1);
        cal.add(Calendar.MINUTE, -59);
        Date fifteenMinutesBack = cal.getTime();
        Date earthquakeDate;
        try {
            earthquakeDate = parser.parse(earthquakeString);
            if(earthquakeDate.after(fifteenMinutesBack)) {
                Log.d("FindMe", "Option: True oldu");
                return true;
            } else {
                Log.d("FindMe", "Option: False oldu");
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

}
