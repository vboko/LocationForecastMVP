package com.test.locationforecastmvp.model.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = "####### DATABASE ######";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "forecast_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ForecastDbModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ForecastDbModel.TABLE_NAME);
        onCreate(db);
    }

    public long insertForecast(String forecast) {
        Log.wtf(LOG_TAG, "---- INSERT ----: " + forecast);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ForecastDbModel.COLUMN_FORECAST_STRING, forecast);
        long id = db.insert(ForecastDbModel.TABLE_NAME, null, values);
        db.close();

        return id;
    }

    public String getLatestForecast() {
        String selectQuery = "SELECT  * FROM " + ForecastDbModel.TABLE_NAME + " ORDER BY " +
                ForecastDbModel.COLUMN_TIMESTAMP + " DESC LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String forecast = "";
        if (cursor.moveToFirst()) {
            forecast = cursor.getString(cursor.getColumnIndex(ForecastDbModel.COLUMN_FORECAST_STRING));
        }
        cursor.close();
        Log.wtf(LOG_TAG, "---- GET ----: " + forecast);
        return forecast;
    }

    public ForecastDbModel getForecastDbModel(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(ForecastDbModel.TABLE_NAME,
                new String[]{ForecastDbModel.COLUMN_ID, ForecastDbModel.COLUMN_FORECAST_STRING, ForecastDbModel.COLUMN_TIMESTAMP},
                ForecastDbModel.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        ForecastDbModel forecastDbModel = null;
        if (cursor != null) {
            cursor.moveToFirst();
            forecastDbModel = new ForecastDbModel(
                    cursor.getInt(cursor.getColumnIndex(ForecastDbModel.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(ForecastDbModel.COLUMN_FORECAST_STRING)),
                    cursor.getString(cursor.getColumnIndex(ForecastDbModel.COLUMN_TIMESTAMP)));
            cursor.close();
        }

        return forecastDbModel;
    }

    public List<ForecastDbModel> getAllForecasts() {
        List<ForecastDbModel> forecasts = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + ForecastDbModel.TABLE_NAME + " ORDER BY " +
                ForecastDbModel.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ForecastDbModel forecastDbModel = new ForecastDbModel();
                forecastDbModel.setId(cursor.getInt(cursor.getColumnIndex(ForecastDbModel.COLUMN_ID)));
                forecastDbModel.setForecast(cursor.getString(cursor.getColumnIndex(ForecastDbModel.COLUMN_FORECAST_STRING)));
                forecastDbModel.setTimestamp(cursor.getString(cursor.getColumnIndex(ForecastDbModel.COLUMN_TIMESTAMP)));

                forecasts.add(forecastDbModel);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return forecasts;
    }

    public int getCount() {
        String countQuery = "SELECT  * FROM " + ForecastDbModel.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int updateForecast(ForecastDbModel forecastDbModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ForecastDbModel.COLUMN_FORECAST_STRING, forecastDbModel.getForecast());

        return db.update(ForecastDbModel.TABLE_NAME, values, ForecastDbModel.COLUMN_ID + " = ?",
                new String[]{String.valueOf(forecastDbModel.getId())});
    }

    public void deleteForecast(ForecastDbModel note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ForecastDbModel.TABLE_NAME, ForecastDbModel.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }
}
