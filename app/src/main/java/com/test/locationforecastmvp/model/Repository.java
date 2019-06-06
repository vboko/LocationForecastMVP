package com.test.locationforecastmvp.model;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.test.locationforecastmvp.AppForecast;
import com.test.locationforecastmvp.model.data.response.ForecastResponse;
import com.test.locationforecastmvp.model.location.UserLocationProvider;
import com.test.locationforecastmvp.model.network.ForecastResultReceiver;
import com.test.locationforecastmvp.model.network.RepositoryService;

import static com.test.locationforecastmvp.model.data.CommonConstants.EXTRA_FORECAST_RESPOSE;
import static com.test.locationforecastmvp.model.data.CommonConstants.EXTRA_LAT;
import static com.test.locationforecastmvp.model.data.CommonConstants.EXTRA_LON;
import static com.test.locationforecastmvp.model.data.CommonConstants.EXTRA_REQUEST_TYPE;
import static com.test.locationforecastmvp.model.data.CommonConstants.EXTRA_SERVICE_ERROR;
import static com.test.locationforecastmvp.model.data.CommonConstants.EXTRA_SERVICE_RECEIVER;
import static com.test.locationforecastmvp.model.data.CommonConstants.SERVICE_RESULT_ERROR;
import static com.test.locationforecastmvp.model.data.CommonConstants.TYPE_DATABASE;
import static com.test.locationforecastmvp.model.data.CommonConstants.TYPE_NETWORK;

public class Repository implements ForecastApi,
        UserLocationProvider.LocationListener,
        ForecastResultReceiver.Receiver {
    private static final String LOG_TAG = "= = = = = Repository =";

    private UserLocationProvider locationProvider;

    private ForecastResultReceiver receiver;

    private ForecastApi.RepositoryServiceCallback<ForecastResponse> callback;

    public Repository(UserLocationProvider locationProvider) {
        this.locationProvider = locationProvider;

        locationProvider.setLocationListener(this);
    }

    @Override
    public void getForecast(RepositoryServiceCallback<ForecastResponse> callback) {
        Log.wtf(LOG_TAG, "getForecast: ");

        this.callback = callback;

        Intent serviceIntent;

        // TODO: 2019-06-05 refactor logic
        if (locationProvider.isServiceReady()) {
            Location location = locationProvider.getLocationFromService();
            if (location != null) {
                serviceIntent = createServiceIntent(AppForecast.getInstance(), location, TYPE_NETWORK);
            } else {
                Log.wtf(LOG_TAG, "getForecast: service returned null location");
                serviceIntent = createServiceIntent(AppForecast.getInstance(), null, TYPE_DATABASE);
            }
        } else {
            Log.wtf(LOG_TAG, "getForecast: service not ready yet, request from DB");
            // Request from db
            serviceIntent = createServiceIntent(AppForecast.getInstance(), null, TYPE_DATABASE);
        }

        AppForecast.getInstance().startService(serviceIntent);
    }

    private Intent createServiceIntent(Context context, @Nullable Location location, int type) {
        Intent intent = new Intent(context, RepositoryService.class);
        receiver = new ForecastResultReceiver(new Handler());
        receiver.setReceiver(this);
        intent.putExtra(EXTRA_SERVICE_RECEIVER, receiver);
        switch (type) {
            case TYPE_NETWORK:
                intent.putExtra(EXTRA_REQUEST_TYPE, TYPE_NETWORK);
                if (location != null) {
                    intent.putExtra(EXTRA_LON, location.getLongitude());
                    intent.putExtra(EXTRA_LAT, location.getLatitude());
                }
                break;
            case TYPE_DATABASE:
                // No need to pass location, we'll retrieve the latest
                // record from DB (if there is some)
                intent.putExtra(EXTRA_REQUEST_TYPE, TYPE_DATABASE);
                break;
        }
        return intent;
    }

    @Override
    public void onLocationUpdated(Location location) {
        Log.wtf(LOG_TAG, "onLocationUpdated: @@@@@@@ LOCATION UPDATED --> Getting forecast @@@@@@@");

        getForecastByLocation(location);
    }

    private void getForecastByLocation(Location location) {
        Log.wtf(LOG_TAG, "         getting forecast by location " + location.getLongitude() + " | " + location.getLatitude());

        Intent serviceIntent = createServiceIntent(AppForecast.getInstance(), location, TYPE_NETWORK);
        AppForecast.getInstance().startService(serviceIntent);
    }

    // Repository service callback
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        Log.wtf(LOG_TAG, "onReceiveResult: ");

        Log.wtf(LOG_TAG, "onReceiveResult: code " + resultCode);
        ForecastResponse forecastResponse = resultData.getParcelable(EXTRA_FORECAST_RESPOSE);

        if (resultCode == SERVICE_RESULT_ERROR) {
            callback.onError(resultData.getString(EXTRA_SERVICE_ERROR));
        } else {
            callback.onResultOk(forecastResponse, resultCode);
        }
    }

    public void onStop() {
        Log.wtf(LOG_TAG, "onStop: ");
        if (receiver != null) {
            receiver.setReceiver(null);
        }
        locationProvider.onStop();
    }
}
