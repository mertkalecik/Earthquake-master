package com.egeuni.earthquake;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class HazardousMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "HazardousMapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1254;
    private static final float DEFAULT_ZOOM = 10f;
    private static final double BASE_LATITUDE = 39.9255;
    private static final double BASE_LONGITUDE = 32.8662;
    private static final float BASE_ZOOM = 4f;

    //variables
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private Event event;
    private ArrayList<Event> eventArrayList;
    private double lat;
    private double lng;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("FindMe", "on map ready çalıştı");
        mMap = googleMap;


        LatLng base = new LatLng(BASE_LATITUDE, BASE_LONGITUDE);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(base, BASE_ZOOM));
        for(int i = 0; i < eventArrayList.size(); i++) {
            event = eventArrayList.get(i);
            lat = Double.parseDouble(event.getLatitude());
            lng = Double.parseDouble(event.getLongitude());
            LatLng latLng = new LatLng(lat, lng);
            addMarker(latLng, event.getPlace());
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Intent intent = getIntent();

        eventArrayList = (ArrayList<Event>) intent.getSerializableExtra("hazard");
        getLocationPermission();
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(HazardousMapActivity.this);
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + event.getLatitude() + ", lng: " + event.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.addMarker(options);
    }

    private void addMarker(LatLng latLng, String title) {
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.addMarker(options);
    }

    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;

        initMap();
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE : {
                if(grantResults.length >0){
                    for(int i = 0; i < grantResults.length ; i++) {
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
                    //initialize map
                    initMap();
                }
            }
        }
    }


}
