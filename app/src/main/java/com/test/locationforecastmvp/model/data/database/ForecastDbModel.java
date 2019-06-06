package com.test.locationforecastmvp.model.data.database;

public class ForecastDbModel {
    public static final String TABLE_NAME = "forecasts";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FORECAST_STRING = "forecast_str";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String forecast;
    private String timestamp;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_FORECAST_STRING + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public ForecastDbModel() {
    }

    public ForecastDbModel(int id, String forecast, String timestamp) {
        this.id = id;
        this.forecast = forecast;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getForecast() {
        return forecast;
    }

    public void setForecast(String forecast) {
        this.forecast = forecast;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
