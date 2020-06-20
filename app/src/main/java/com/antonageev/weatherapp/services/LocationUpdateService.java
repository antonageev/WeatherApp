package com.antonageev.weatherapp.services;

import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.antonageev.weatherapp.MainActivity;

import java.io.IOException;
import java.util.List;

public class LocationUpdateService extends Service {

    private final String TAG = this.getClass().getSimpleName();
    private Location currentLocation;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requestLocation();
        stopSelf();
        return START_NOT_STICKY;
    }

    private void requestLocation(){
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location;
                getCityFromLocation(currentLocation);
                Log.wtf(TAG, " onLocationChanged: " + currentLocation);
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
        };

        String provider = locationManager.getBestProvider(criteria, true);

        if (provider != null){
            try {
                Log.wtf(TAG, " requestLocation(): provider NOT NULL and launched getLastKnownLocation");
                currentLocation = locationManager.getLastKnownLocation(provider);
                Log.wtf(TAG, " requestLocation(): getLastKnownLocation: " + currentLocation);
                locationManager.requestLocationUpdates(provider, 10000, 10, locationListener);
                Log.wtf(TAG, " requestLocation(): requestLocationUpdates: " + currentLocation);
            } catch (SecurityException e){
                e.printStackTrace();
            }

        }
        getCityFromLocation(currentLocation);
    }

    private void getCityFromLocation(Location location){
        Geocoder geocoder = new Geocoder(this);
        if (location != null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String cityName = "Not found";
                        List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 3);

                        for (Address adr : listAddress) {
                            if (adr != null){
                                String city = adr.getLocality();
                                if (city!=null && !city.equals("")){
                                    cityName = city;
                                    Log.d("CITY_EXTRACT_FROM_LOC", "run: " + cityName);
                                    Intent intent = new Intent(MainActivity.LOCATION_INTENT_FILTER);
                                    intent.putExtra("cityToShow", cityName);
                                    sendBroadcast(intent);
                                    break;
                                }
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            Log.wtf(TAG, "getCityFromLocation: location is null");
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
