package com.test.locationforecastmvp.util;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtil {
    private static final String LOG_TAG = "NetworkUtil";

    public static final String API_KEY = "ed3724d4850e07c530135c5488eba79c";

    public static final String FORECAST_REQUEST = "https://api.openweathermap.org/data/2.5/weather?lat=%1$s&lon=%2$s&units=metric&APPID=" + API_KEY;

    public static URL getLatLonRequestUrl(@NonNull String lat, @NonNull String lon) throws MalformedURLException {
        return new URL(String.format(FORECAST_REQUEST, lat, lon));
    }

    public static boolean isConnectedToInternet() {
        boolean isConnected = false;
        try {
            URL url = new URL("https://google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            isConnected = connection.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(LOG_TAG, "isConnectedToInternet: " + isConnected);
        return isConnected;
    }
}
