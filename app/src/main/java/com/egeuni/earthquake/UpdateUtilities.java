package com.egeuni.earthquake;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

public class UpdateUtilities {

    private static final int UPDATE_INTERVAL_MINUTES = 1;
    private static final int UPDATE_INTERVAL_SECONDS = (int)(TimeUnit.MINUTES.toSeconds(UPDATE_INTERVAL_MINUTES));
    private static final int SYNC_FLEXTIME_SECONDS = UPDATE_INTERVAL_SECONDS;

    private static final String UPDATE_JOB_TAG = "earthquake_update_tag";

    private static boolean sInitialized = false;

    synchronized public static void scheduleUpdate(@NonNull final Context context) {
        if (sInitialized) return;
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job contraintUpdateJob = dispatcher.newJobBuilder()
                .setService(UpdateFirebaseJobService.class)
                .setTag(UPDATE_JOB_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        UPDATE_INTERVAL_SECONDS,
                        UPDATE_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();
        dispatcher.schedule(contraintUpdateJob);
        sInitialized = true;
    }
}
