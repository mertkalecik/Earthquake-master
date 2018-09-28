package com.egeuni.earthquake;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EarthquakeDataAdapter.EarthquakeAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private EarthquakeDataAdapter mEartquakeAdapter;
    private ProgressBar mLoadingIndicator;
    private boolean isFirstTime;
    private ArrayList<Event> extractedEventList;
    private String KEY_IS_FIRST;
    private SharedPreferences mPref;

    private static final String KOERI_REQUEST_URL = "http://www.koeri.boun.edu.tr/scripts/lst0.asp";

    private AppDatabase mDb;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KEY_IS_FIRST = getString(R.string.pref_is_first);
        Context context = getApplicationContext();
        mPref = context.getSharedPreferences(KEY_IS_FIRST, Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        isFirstTime = true;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mEartquakeAdapter = new EarthquakeDataAdapter(MainActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mDb = AppDatabase.getsInstance(getApplicationContext());

        String isFirst = mPref.getString(KEY_IS_FIRST, null);

        if(isFirst == null) {
            loadEarthquakeData();
        } else {
            populateUI();
        }
    }

    public void afterEarthquakeDataFetched() {
        Thread thread = new Thread() {
            public void run() {
                mDb.taskDao().delete();
            }
        };
        thread.run();

        for (Event e:extractedEventList) {
            String place = e.getPlace();
            String date = e.getDate();
            String hour = e.getHour();
            String mag = e.getMag();
            String depth = e.getDepth();
            String latitude = e.getLatitude();
            String longitude = e.getLongitude();

            TaskEntry taskEntry = new TaskEntry(place, date, hour,mag, depth, latitude, longitude);
            mDb.taskDao().insertTask(taskEntry);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void populateUI() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mLoadingIndicator.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                List<TaskEntry> taskEntries = mDb.taskDao().loadAllTasks();
                ArrayList<Event> events = new ArrayList<Event>();
                for (TaskEntry task: taskEntries) {
                    Event event = new Event(task.getPlace(), task.getDate(), task.getHour(), task.getMag(), task.getDepth(),
                    task.getLatitude(), task.getLongitude());
                    events.add(event);
                }
                mEartquakeAdapter.setEarthquakeData(events);
                mRecyclerView.setAdapter(mEartquakeAdapter);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public void onClick(Event e) {
        Intent i = new Intent(this, DetailsActiviy.class);
        i.putExtra("object", e);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void loadEarthquakeData() {
        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        setPreference(KEY_IS_FIRST, "true");
    }

    private void setPreference (String key, String value) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public class EarthquakeAsyncTask extends AsyncTask<Void,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(Void... strs) {
            String response = null;
            try {
                response = Utils.fetchEarthquakeData(KOERI_REQUEST_URL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            extractedEventList = Utils.extractFeatureFromResult(result);
            mRecyclerView.setAdapter(mEartquakeAdapter);
            mEartquakeAdapter.setEarthquakeData(extractedEventList);
            mRecyclerView.setVisibility(View.VISIBLE);
            afterEarthquakeDataFetched();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.item_refresh) {
            loadEarthquakeData();

            mRecyclerView.setVisibility(View.INVISIBLE);

        }

        if (itemId == R.id.item_settings) {
            Toast.makeText(this,"Settings button is pressed.", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
