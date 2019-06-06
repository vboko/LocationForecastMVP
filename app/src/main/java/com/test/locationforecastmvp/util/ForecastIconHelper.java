package com.test.locationforecastmvp.util;

import android.support.annotation.DrawableRes;

import com.test.locationforecastmvp.R;

import java.util.NavigableMap;
import java.util.TreeMap;

public class ForecastIconHelper {

    @DrawableRes
    public static int getIconForWeatherCondition(int conditionId) {
        return propertiesCodes.get(propertiesCodes.floorKey(conditionId)).getWeatherIcon();
    }

    // https://openweathermap.org/weather-conditions
    private static final NavigableMap<Integer, WeatherConditionProperties> propertiesCodes;
    static {
        propertiesCodes = new TreeMap<>();
        propertiesCodes.put(200, new WeatherConditionProperties(R.drawable.ic_weather_lightning));
        propertiesCodes.put(300, new WeatherConditionProperties(R.drawable.ic_weather_pouring));
        propertiesCodes.put(400, new WeatherConditionProperties(R.drawable.ic_looks)); // stub
        propertiesCodes.put(500, new WeatherConditionProperties(R.drawable.ic_weather_rainy));
        propertiesCodes.put(511, new WeatherConditionProperties(R.drawable.ic_weather_pouring));
        propertiesCodes.put(600, new WeatherConditionProperties(R.drawable.ic_weather_snowy));
        propertiesCodes.put(700, new WeatherConditionProperties(R.drawable.ic_weather_fog));
        propertiesCodes.put(800, new WeatherConditionProperties(R.drawable.ic_weather_sunny));
        propertiesCodes.put(801, new WeatherConditionProperties(R.drawable.ic_weather_partlycloudy));
        propertiesCodes.put(802, new WeatherConditionProperties(R.drawable.ic_weather_cloudy));
        propertiesCodes.put(900, new WeatherConditionProperties(R.drawable.ic_looks)); //stub
    }

    private static class WeatherConditionProperties {
        @DrawableRes
        private int weatherIcon;

        WeatherConditionProperties(@DrawableRes int weatherIcon) {
            this.weatherIcon = weatherIcon;
        }

        @DrawableRes
        public int getWeatherIcon() {
            return weatherIcon;
        }

        public void setWeatherIcon(@DrawableRes int weatherIcon) {
            this.weatherIcon = weatherIcon;
        }
    }
}
