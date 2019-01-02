package com.egeuni.earthquake;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity implements EarthquakeDataAdapter.EarthquakeAdapterOnClickHandler,
        NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private DrawerLayout drawer;
    private EarthquakeDataAdapter mEartquakeAdapter;
    private ProgressBar mLoadingIndicator;
    private boolean isFirstTime;
    private ArrayList<Event> extractedEventList;
    private String KEY_IS_FIRST;
    private SharedPreferences mPref;
    private TextView titlePlace;
    private ImageView mMapButton;
    private ImageView mBackButton;
    private ImageView mHomeButton;
    @BindView(R.id.nav_person)
    ImageView mPersonButton;
    private static AppComponent component;

    private static final String KOERI_REQUEST_URL = "http://www.koeri.boun.edu.tr/scripts/lst0.asp";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    private AppDatabase mDb;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KEY_IS_FIRST = getString(R.string.pref_is_first);
        Context context = getApplicationContext();
        mPref = context.getSharedPreferences(KEY_IS_FIRST, Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        initUI();
        initNavigationBar();
        isFirstTime = true;
        Utils utils = new Utils(getApplicationContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        component = DaggerAppComponent.builder().contextModule(new ContextModule(getApplicationContext())).build();
        mEartquakeAdapter = new EarthquakeDataAdapter(MainActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mDb = AppDatabase.getsInstance(context);

        String isFirst = mPref.getString(KEY_IS_FIRST, null);

        if(isFirst == null) {
            loadEarthquakeData();

        } else {
            populateUI();
        }

        NeuralNetworkUtilities neuralNetworkUtilities = NeuralNetworkUtilities.getSingletonInstance();

        UpdateUtilities.scheduleUpdate(this);
    }

    public static AppComponent getMyComponent() {
        return component;
    }

    private void initUI(){
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mMapButton = (ImageView) findViewById(R.id.iv_second);
        mBackButton = (ImageView) findViewById(R.id.iv_first);
        mHomeButton = (ImageView) findViewById(R.id.iv_third);
        titlePlace = (TextView)findViewById(R.id.tv_title_place);
        titlePlace.setText(getString(R.string.title_place));
    }

    private void initNavigationBar(){
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Log.d("FindMe", "Home button is clicked");
                if(drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                break;

            case R.id.nav_map:
                Intent i = new Intent(this, MapActivity.class);
                if (extractedEventList == null) {
                    fillEventList();
                }
                i.putExtra("type", 1);
                i.putExtra("object", extractedEventList);
                startActivity(i);
                break;

            case R.id.nav_person:
                Intent intnt = new Intent(this, PersonActivity.class);
                startActivity(intnt);
                break;

            case R.id.nav_hazardous:
                new AsyncTask<Void,Void, ArrayList<Event>>() {

                    @Override
                    protected ArrayList<Event> doInBackground(Void... voids) {
                        List<TaskEntry> events = mDb.taskDao().loadAllTasks();
                        NeuralNetworkUtilities neuralUtil = NeuralNetworkUtilities.getSingletonInstance();
                        return neuralUtil.extractRiskyEvents(events);
                    }

                    @Override
                    protected void onPostExecute(ArrayList<Event> events) {
                        super.onPostExecute(events);
                        Intent in = new Intent(MainActivity.this, HazardousMapActivity.class);
                        in.putExtra("hazard", events);
                        startActivity(in);
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case R.id.nav_info:
                Intent intentInfo = new Intent(this, AboutActivity.class);
                startActivity(intentInfo);
                break;

            case R.id.nav_send:
                Intent intentSend = new Intent(this, SendEmailActivity.class);
                startActivity(intentSend);
                break;


            default:
                break;
        }

        return true;
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS) {
            //everything is OK.
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "We can't make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
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

    private void fillEventList() {
        Thread t = new Thread() {
            public void run() {
                List<TaskEntry> list = mDb.taskDao().loadAllTasks();
                extractedEventList = new ArrayList<Event>();
                for (TaskEntry e : list) {
                    extractedEventList.add(new Event(
                            e.getPlace(),
                            e.getDate(),
                            e.getHour(),
                            e.getMag(),
                            e.getDepth(),
                            e.getLatitude(),
                            e.getLongitude(),
                            e.getHazardDepth(),
                            e.getHazardMag()));
                }
                Log.d("FindMe", extractedEventList.size() +"");
            }
        };
        t.run();
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
                mDb = AppDatabase.getsInstance(getApplicationContext());
                List<TaskEntry> taskEntries = mDb.taskDao().loadAllTasks();
                ArrayList<Event> events = new ArrayList<Event>();
                for (TaskEntry task: taskEntries) {
                    Event event = new Event(task.getPlace(), task.getDate(), task.getHour(), task.getMag(), task.getDepth(),
                    task.getLatitude(), task.getLongitude(), task.getHazardDepth(), task.getHazardMag());
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
            Intent settingsIntent = new Intent(this, SettingsPreferenceActivity.class);
            startActivity(settingsIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
