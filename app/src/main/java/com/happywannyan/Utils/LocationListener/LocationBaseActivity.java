package com.happywannyan.Utils.LocationListener;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.happywannyan.Events;

/**
 * Created by Yahya Bayramoglu on 10/02/16.
 */
public abstract class LocationBaseActivity extends AppCompatActivity implements LocationListener {
Events events;
    private MyLocalLocationManager locationManager;

    public abstract LocationConfiguration getLocationConfiguration();

    public abstract void onLocationFailed(int failType);

    public abstract void onLocationChanged(Location location);

    public MyLocalLocationManager getLocationManager() {
        return locationManager;
    }

    public void getLocation(Events events) {
        this.events=events;
        if (locationManager != null) {
            locationManager.get();
            Log.d("LocationBaseActivity","\"Couldn't get location, because network is not accessible!\"");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = new MyLocalLocationManager(getLocationConfiguration()).on(this).notify(locationReceiver);
    }

    @Override
    protected void onDestroy() {
        locationManager.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        locationManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private final LocationReceiver locationReceiver = new LocationReceiver() {

        @Override
        public void onLocationChanged(Location location) {
            LocationBaseActivity.this.onLocationChanged(location);
            events.UpdateLocation(location);
        }

        @Override
        public void onLocationFailed(int failType) {
            Log.d("LocationBaseActivity"," OFF"+failType);
            LocationBaseActivity.this.onLocationFailed(failType);
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            LocationBaseActivity.this.onStatusChanged(provider, status, extras);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("LocationBaseActivity"," Enable"+provider);
            LocationBaseActivity.this.onProviderEnabled(provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("LocationBaseActivity"," Desable"+provider);
            LocationBaseActivity.this.onProviderDisabled(provider);
        }
    };

}