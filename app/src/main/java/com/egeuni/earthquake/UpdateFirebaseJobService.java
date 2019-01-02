package com.egeuni.earthquake;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.concurrent.ExecutionException;

public class UpdateFirebaseJobService extends JobService {

    private AsyncTask mBackgroundTask;

    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onStartJob(final JobParameters job) {
        mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = UpdateFirebaseJobService.this;
                UpdateTask.executeTask(context, UpdateTask.ACTION_UPDATE_EARTHQUAKES);

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
            }
        };

        mBackgroundTask.execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if( mBackgroundTask != null) mBackgroundTask.cancel(true);
        return true;
    }
}
