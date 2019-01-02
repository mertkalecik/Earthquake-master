package com.egeuni.earthquake;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.DoubleBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;

import javax.inject.Inject;

public final class Utils {


    public static final String LOG_TAG = Utils.class.getSimpleName();
    public static final int LAST_INDEX = 79;

    private static Context appContext;

    public Utils(Context context) {
        appContext = context;
    }

    public static String fetchEarthquakeData(String requestUrl) throws IOException {

        URL url = createUrl(requestUrl);
        //String url1 = "http://www.koeri.boun.edu.tr/scripts/lst0.asp";
        org.jsoup.nodes.Document doc = Jsoup.connect(url.toString()).get();

        Elements bodies = doc.select("pre");

        String response = bodies.text();

        //return earthquake;
        return response;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    public static ArrayList<Event> extractFeatureFromResult(String earthquakeString) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeString)) {
            return null;
        }

        String cleanData = getCleanData(earthquakeString);
        ArrayList<Event> resultArray = writeEarthquakeData(cleanData);

        return resultArray;
    }

    public static String getCleanData(String data) {
        String[] parts = data.split("---------- --------  --------  -------   ----------    ------------    --------------                                  --------------");

        return  parts[1];
    }

    public static ArrayList<Event> writeEarthquakeData(String cleanData) {
        ArrayList<Event> arrayList = new ArrayList<Event>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        String numEarthquakes = sharedPreferences.getString("listPref", "500");
        String minMag = sharedPreferences.getString("listPrefMag", "1.7");
        String minDepth = sharedPreferences.getString(SettingsPreferenceActivity.KEY_LIST_DEPTH_PREFERENCE, "20.0");
        /** Fake Data*/
        Event e1 = new Event("Uncubozköy Mahallesi (Manisa)", "2018.12.27", "15:50:20", "7.0", "1.3","39.88921","34.65432", 1, 1);
        Event e2 = new Event("Bornova Ege Üniversitesi (İzmir)", "2018.12.27", "06:47:20", "5.0", "1.3","39.88921","34.65432", 1,1);
        arrayList.add(e1);
        arrayList.add(e2);
        Scanner scanner = new Scanner(cleanData);
        scanner.nextLine();
        int i = 0;
        double currentMag;
        double currentDepth;
        while (i < Integer.parseInt(numEarthquakes)) {
              String record = scanner.nextLine();
              String mag = record.substring(60,63);
              currentMag = Double.parseDouble(mag);
              if(currentMag >= Double.parseDouble(minMag)) {
                  String depth = record.substring(46, 49);
                  currentDepth = Double.parseDouble(depth);
                  if(currentDepth <= Double.parseDouble(minDepth)) {
                      String place = getPlace(record.substring(71, record.length()));
                      String date = record.substring(0, 10);
                      String hour = record.substring(11, 19);
                      String latitude = record.substring(21, 28);
                      String longitude = record.substring(31, 38);

                      int hDepth = 0, hMag = 0;
                      if(Double.parseDouble(depth) <= 10.0) {
                          hDepth = 1;
                      }
                      if(Double.parseDouble(mag) >= 3.7) {
                          hMag = 1;
                      }

                      Event event = new Event(place, date, hour, mag, depth, latitude, longitude, hDepth, hMag);
                      arrayList.add(event);
                      i++;
                  }
              }
              i++;
        }
        scanner.close();
        return arrayList;
    }

    public static String getPlace(String place) {
        int count = 0,lastIndex = 0;
        for(int i = 0; i <= place.length(); i++) {
            if(count == 3) {
                lastIndex = i;
                break;
            } else if(place.charAt(i) == ' ') {
                count++;
            }
        }

        int index = 0;
        String result = place.substring(0, lastIndex);
        while (index < lastIndex) {
            if(place.charAt(index) == '(')
                result = place.substring(0,index-1) + "\n" + place.substring(index, lastIndex);
            if(place.charAt(index) == '-')
                result = place.substring(0,index-1) + "\n" + place.substring(index+1,lastIndex);

            index++;
        }

        return result;
    }
}