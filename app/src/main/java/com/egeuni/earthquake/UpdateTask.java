package com.egeuni.earthquake;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;
import javax.inject.Named;

public class UpdateTask {

    public static final String ACTION_UPDATE_EARTHQUAKES = "update-earthquakes";
    public static final String ACTION_UPDATE_EARTHQUAKES_PREFERENCE = "update-earthquakes-pref";
    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";
    private static final String KOERI_REQUEST_URL = "http://www.koeri.boun.edu.tr/scripts/lst0.asp";
    private static ArrayList<Event> extractedEventList;
    private static AppDatabase mDb;
    private static ArrayList<ReportEvent> reportEvents;
    private static List<TaskEntry> taskEntries;
    private static Context _context;

    public static void executeTask(Context context, String action) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isUpdateControl = preferences.getBoolean(SettingsPreferenceActivity.KEY_SWITCH_PREFERENCE, true);
        Log.d("Mk", "isUpdateControl " + isUpdateControl);
        if(isUpdateControl || action.equals(ACTION_UPDATE_EARTHQUAKES_PREFERENCE)) {
            if(ACTION_UPDATE_EARTHQUAKES.equals(action) || ACTION_UPDATE_EARTHQUAKES_PREFERENCE.equals(action)){
                updateEarthquakeData(context);
            } else if(ACTION_DISMISS_NOTIFICATION.equals(action)) {
                NotificationUtils.clearAllNotifications(context);
            }
        }
    }

    private static void updateEarthquakeData(Context context) {
        //NotificationUtils.remindUserBecauseUpdate(context);
        mDb = AppDatabase.getsInstance(context);
        _context = context;
        UpdateAsyncTask task = new UpdateAsyncTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private static void checkEarthquakeForUser(){
        Log.d("FindMe", "CheckEarthquakeForUser is working now.");
        CheckAsyncTask cst = new CheckAsyncTask();
        cst.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static class CheckAsyncTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            taskEntries = mDb.taskDao().loadAllTasks();
            Formatter formatter = new Formatter();
            int index = 0;
            for (TaskEntry t: taskEntries) {
                if(Double.parseDouble(t.getMag()) >= 4.0) {
                    Log.d("FindMe", "4.0 üzerinde depremi gördü...");
                    String place = formatter.extractCity(t.getPlace());
                    Log.d("FindMe", "Place: " + place);
                    List<TaskUser> users = mDb.taskDao().loadAllProfiles();
                    if(place != null) {
                        for (TaskUser user : users) {
                            if(place.toUpperCase().equals(user.getPlace().toUpperCase())) {
                                if(formatter.isNewEarthquake(t.getDate()+ " " +t.getHour())) {
                                    NotificationUtils.remindUserBecauseEarthquake(_context, user.getName(), user.getPlace());
                                }

                            }
                        }
                    }
                }
                index++;
            }
            return null;
        }
    }


    public static class UpdateAsyncTask extends AsyncTask<Void,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... strs) {
            String response = null;
            try {
                response = Utils.fetchEarthquakeData(KOERI_REQUEST_URL);
                mDb.taskDao().delete();
                Log.d("FindMe","mDb's records are deleted.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            extractedEventList = Utils.extractFeatureFromResult(result);
            afterEarthquakeDataFetched();
            checkEarthquakeForUser();
        }
    }

    public static void afterEarthquakeDataFetched() {
        Log.d("FindMe","afterEarthquakeDataFetched is working");
        for (Event e:extractedEventList) {
            String place = e.getPlace();
            String date = e.getDate();
            String hour = e.getHour();
            String mag = e.getMag();
            String depth = e.getDepth();
            String latitude = e.getLatitude();
            String longitude = e.getLongitude();

            int hDepth = 0, hMag = 0;
            if(Double.parseDouble(depth) <= 10.0) {
                hDepth = 1;
            }
            if(Double.parseDouble(mag) >= 3.7) {
                hMag = 1;
            }

            TaskEntry taskEntry = new TaskEntry(place, date, hour,mag, depth, latitude, longitude, hDepth, hMag);
            mDb.taskDao().insertTask(taskEntry);
        }
    }

}
