package com.test.locationforecastmvp.model.network;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.test.locationforecastmvp.AppForecast;
import com.test.locationforecastmvp.model.data.database.DatabaseHelper;
import com.test.locationforecastmvp.model.data.response.ForecastResponse;
import com.test.locationforecastmvp.util.NetworkUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.test.locationforecastmvp.model.data.CommonConstants.ERROR_EMPTY_DB;
import static com.test.locationforecastmvp.model.data.CommonConstants.EXTRA_FORECAST_RESPOSE;
import static com.test.locationforecastmvp.model.data.CommonConstants.EXTRA_LAT;
import static com.test.locationforecastmvp.model.data.CommonConstants.EXTRA_LON;
import static com.test.locationforecastmvp.model.data.CommonConstants.EXTRA_REQUEST_TYPE;
import static com.test.locationforecastmvp.model.data.CommonConstants.EXTRA_SERVICE_ERROR;
import static com.test.locationforecastmvp.model.data.CommonConstants.EXTRA_SERVICE_RECEIVER;
import static com.test.locationforecastmvp.model.data.CommonConstants.SERVICE_RESULT_DB_OK;
import static com.test.locationforecastmvp.model.data.CommonConstants.SERVICE_RESULT_ERROR;
import static com.test.locationforecastmvp.model.data.CommonConstants.SERVICE_RESULT_NETWORK_OK;
import static com.test.locationforecastmvp.model.data.CommonConstants.TYPE_DATABASE;
import static com.test.locationforecastmvp.model.data.CommonConstants.TYPE_NETWORK;


public class RepositoryService extends IntentService {
    private static final String LOG_TAG = "RepositoryService";

    private static final String REQUEST_METHOD_GET = "GET";

    private ResultReceiver resultReceiver;

    private DatabaseHelper db;

    public RepositoryService() {
        super("RepositoryService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        resultReceiver = intent.getParcelableExtra(EXTRA_SERVICE_RECEIVER);
        db = new DatabaseHelper(AppForecast.getInstance());

        if (!intent.hasExtra(EXTRA_REQUEST_TYPE)) {
            throw new IllegalArgumentException("Must include TYPE in service intent");
        }

        Log.wtf(LOG_TAG, "onHandleIntent: *********** TYPE = " + intent.getIntExtra(EXTRA_REQUEST_TYPE, 0));

        switch (intent.getIntExtra(EXTRA_REQUEST_TYPE, 0)) {
            case TYPE_DATABASE:
                requestFromDatabase();
                break;
            case TYPE_NETWORK:
                requestFromNetwork(intent);
                break;
        }
    }

    @Nullable
    private ForecastResponse getForecastByLatLon(String lat, String lon) {
        Log.wtf(LOG_TAG, "getForecastByLatLon: ");

        try {
            String response = sendGetRequest(NetworkUtil.getLatLonRequestUrl(lat, lon));

            // Something went wrong during posting GET request
            if (response == null) {
                return null;
            }

            if (!response.isEmpty()) {
                Gson gson = new Gson();
                ForecastResponse forecastResponse = gson.fromJson(response, ForecastResponse.class);
                db.insertForecast(response);
                return forecastResponse;
            } else {
                // TODO: 2019-06-02 что-то делать если ответ будет пустым
                onError("Empty server response");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onError(e.getMessage());
        }
        return null;
    }

    public void onResultOk(ForecastResponse forecastResponse, int resultCode) {
        Log.wtf(LOG_TAG, "onResultOk: ");
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_FORECAST_RESPOSE, forecastResponse);
        resultReceiver.send(resultCode, bundle);
    }

    public void onError(String errorMessage) {
        Log.wtf(LOG_TAG, "onError: ");
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_SERVICE_ERROR, errorMessage);
        resultReceiver.send(SERVICE_RESULT_ERROR, bundle);
    }

    private void requestFromDatabase() {
        Log.wtf(LOG_TAG, "onHandleIntent: getting forecast from ==== DATABASE ====");
        String forecastDbModel = db.getLatestForecast();
        if (!forecastDbModel.isEmpty()) {
            Gson gson = new Gson();
            ForecastResponse forecastResponse = gson.fromJson(forecastDbModel, ForecastResponse.class);
            onResultOk(forecastResponse, SERVICE_RESULT_DB_OK);
        } else {
            Log.d(LOG_TAG, "onHandleIntent: db is empty");
            onError(ERROR_EMPTY_DB);
        }
    }

    private void requestFromNetwork(Intent intent) {
        if (NetworkUtil.isConnectedToInternet()) {
            Log.wtf(LOG_TAG, "requestFromNetwork: getting forecast from ==== NETWORK ====");
            Double lat = intent.getDoubleExtra(EXTRA_LAT, 0);
            Double lon = intent.getDoubleExtra(EXTRA_LON, 0);
            ForecastResponse forecastResponse = getForecastByLatLon(lat.toString(), lon.toString());
            if (forecastResponse != null) {
                onResultOk(forecastResponse, SERVICE_RESULT_NETWORK_OK);
            } else {
                // TODO: 2019-06-04
                //??
                onError("wtf?");
            }
        } else {
            Log.wtf(LOG_TAG, "requestFromNetwork: not connected to internet, requesting from DB");
            requestFromDatabase();
        }
    }

    private String sendGetRequest(URL url) {
        Log.d(LOG_TAG, "sendGetRequest: ");

        String response;
        HttpURLConnection connection = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        StringBuilder readStringBuilder = new StringBuilder();

        try {
            // Open http connection
            connection = (HttpURLConnection) url.openConnection();
            // Set request method
            connection.setRequestMethod(REQUEST_METHOD_GET);
            // Connection timeout & read timeout
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            int statusCode = connection.getResponseCode();
            Log.wtf(LOG_TAG, "sendGetRequest:      Server response = " + statusCode);
            if (statusCode == HttpURLConnection.HTTP_OK) {

                // Input stream from url connection
                InputStream inputStream = connection.getInputStream();

                inputStreamReader = new InputStreamReader(inputStream);

                bufferedReader = new BufferedReader(inputStreamReader);

                // Reading server response
                String line = bufferedReader.readLine();

                while (line != null) {
                    // Gather all lines in string buffer
                    readStringBuilder.append(line);
                    // Read line
                    line = bufferedReader.readLine();
                }
                response = readStringBuilder.toString();

                return response;
            } else {
                onError("Something went wrong, response code " + statusCode);
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onError(e.getMessage());
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            onError(e.getMessage());
            return null;
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                onError(e.getMessage());
            }
        }
    }
}