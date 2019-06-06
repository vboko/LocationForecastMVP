package com.test.locationforecastmvp.model.data;

public class CommonConstants {
    public static final String REQUEST_NN = "http://api.openweathermap.org/data/2.5/weather?lat=56.327530&lon=44.000717&units=metric&APPID=ed3724d4850e07c530135c5488eba79c";

    public static final String EXTRA_SERVICE_RECEIVER = "com.test.locationforecastmvp.EXTRA_SERVICE_RECEIVER";
    public static final String EXTRA_LAT = "com.test.locationforecastmvp.EXTRA_LAT";
    public static final String EXTRA_LON = "com.test.locationforecastmvp.EXTRA_LON";
    public static final String EXTRA_FORECAST_RESPOSE = "com.test.locationforecastmvp.EXTRA_FORECAST_RESPOSE";
    public static final String EXTRA_SERVICE_ERROR = "com.test.locationforecastmvp.EXTRA_SERVICE_ERROR";
    public static final String EXTRA_LOCATION = "com.test.locationforecastmvp.EXTRA_LOCATION";
    public static final String EXTRA_REQUEST_TYPE = "com.test.locationforecastmvp.EXTRA_REQUEST_TYPE";

    public static final String ACTION_LOCATION_UPDATED = "com.test.locationforecastmvp.ACTION_LOCATION_UPDATED";

    public static final int SERVICE_RESULT_NETWORK_OK = 777;
    public static final int SERVICE_RESULT_DB_OK = 888;
    public static final int SERVICE_RESULT_ERROR = 1984;

    public static final String ERROR_EMPTY_DB = "No forecast records in DB";

    public static final int TYPE_NETWORK = 100;
    public static final int TYPE_DATABASE = 900;
}
