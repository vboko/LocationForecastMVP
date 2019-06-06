package com.test.locationforecastmvp.util;

import android.databinding.BindingAdapter;
import android.util.Log;
import android.widget.ImageView;

import static com.test.locationforecastmvp.util.ForecastIconHelper.getIconForWeatherCondition;

public class BindingAdapters {
    private static final String LOG_TAG = "BindingAdapters";

    @BindingAdapter("forecastIcon")
    public static void getForecastIcon(ImageView imageView, int forecastDesc) {
        Log.wtf(LOG_TAG, "getForecastIcon: " + forecastDesc);
        if (forecastDesc != 0) {
            imageView.setImageResource(getIconForWeatherCondition(forecastDesc));
        }
    }
}
