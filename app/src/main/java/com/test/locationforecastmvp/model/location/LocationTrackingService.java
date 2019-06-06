package com.test.locationforecastmvp.model.location;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import static com.test.locationforecastmvp.model.data.CommonConstants.ACTION_LOCATION_UPDATED;
import static com.test.locationforecastmvp.model.data.CommonConstants.EXTRA_LOCATION;

public class LocationTrackingService extends Service {
    private static final String LOG_TAG = "<< LOCATION SERVICE >>";

    private LocationManager locationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 100f;

    private class LocationListener implements android.location.LocationListener {
        Location lastLocation;

        LocationListener(String provider) {
            lastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.wtf(LOG_TAG, "onLocationChanged:         ****** LOCATION WAS UPDATED ****** by " + location.getProvider());
            Log.wtf(LOG_TAG, "onLocationChanged: " + location);
            lastLocation.set(location);

            Intent intent = new Intent(ACTION_LOCATION_UPDATED);
            intent.putExtra(EXTRA_LOCATION, location);
            LocalBroadcastManager.getInstance(LocationTrackingService.this).sendBroadcast(intent);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(LOG_TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(LOG_TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(LOG_TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] locationListeners = new LocationListener[]{new LocationListener(LocationManager.GPS_PROVIDER), new LocationListener(LocationManager.NETWORK_PROVIDER)};

    // Use Binder for getting data from the service
    public class LocalBinder extends Binder {
        public LocationTrackingService getService() {
            return LocationTrackingService.this;
        }
    }

    private final IBinder binder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /*@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(LOG_TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }*/

    @Override
    public void onCreate() {
        super.onCreate();

        Log.wtf(LOG_TAG, "onCreate: ");

        initLocationManager();

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, locationListeners[0]);
        } catch (SecurityException e) {
            // Couldn't request location update
            Log.wtf(LOG_TAG, "onCreate: " + e.getMessage());
        } catch (IllegalArgumentException ex) {
            Log.e(LOG_TAG, "gps provider does not exist " + ex.getMessage());
        }

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, locationListeners[1]);
        } catch (SecurityException e) {
            // Couldn't request location update
            Log.wtf(LOG_TAG, "onCreate: " + e.getMessage());
        } catch (IllegalArgumentException ex) {
            Log.d(LOG_TAG, "network provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.e(LOG_TAG, ".............xxxxxxx onDestroy");
        super.onDestroy();
        if (locationManager != null) {
            for (LocationListener locationListener : locationListeners) {
                try {
                    locationManager.removeUpdates(locationListener);
                } catch (Exception e) {
                    Log.wtf(LOG_TAG, "failed to remove location listeners, ignore", e);
                }
            }
        }
    }

    private void initLocationManager() {
        Log.wtf(LOG_TAG, "initLocationManager: ");
        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Nullable
    public Location getLastLocation() {
        Log.wtf(LOG_TAG, "getLastLocation: ");

        Location lastLocation = null;

        if (locationListeners[0].lastLocation != null) {
            lastLocation = locationListeners[0].lastLocation;
            if (lastLocation.getLatitude() == 0.0) {
                // GPS is not ready yet and returning `Earth` (0.0, 0.0)
                // so we need to use location from the network
                if (locationListeners[1].lastLocation != null) {
                    lastLocation = locationListeners[1].lastLocation;
                }
            }
        } else if (locationListeners[1].lastLocation != null) {
            lastLocation = locationListeners[1].lastLocation;
        }
        Log.wtf(LOG_TAG, "getLastLocation: " + lastLocation);
        return lastLocation;
    }
}
