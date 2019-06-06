package com.test.locationforecastmvp.model.data.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.test.locationforecastmvp.model.data.response.ForecastResponse;


public class ForecastViewModel implements Parcelable {
    private String lat;
    private String lon;
    private String description;
    private String temp;
    private String tempMax;
    private String tempMin;
    private String humidity;
    private String pressure;
    private String name;
    private int conditionId;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public int getConditionId() {
        return conditionId;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    public ForecastViewModel(ForecastResponse forecastResponse) {
        if (forecastResponse.getCoord() != null) {
            this.lat = String.valueOf(forecastResponse.getCoord().getLat());
            this.lon = String.valueOf(forecastResponse.getCoord().getLon());
        }
        if (forecastResponse.getWeather() != null) {
            if (forecastResponse.getWeather().get(0) != null
                    && forecastResponse.getWeather().get(0).getDescription() != null) {
                this.description = forecastResponse.getWeather().get(0).getDescription();
                this.conditionId = forecastResponse.getWeather().get(0).getId();
            }
        }
        if (forecastResponse.getMain() != null) {
            this.temp = String.valueOf(Double.valueOf(forecastResponse.getMain().getTemp()).intValue());
            this.tempMax = String.valueOf(forecastResponse.getMain().getTempMax());
            this.tempMin = String.valueOf(forecastResponse.getMain().getTempMin());
            this.humidity = String.valueOf(forecastResponse.getMain().getHumidity());
            this.pressure = String.valueOf(forecastResponse.getMain().getPressure());
        }
        if (forecastResponse.getName() != null) {
            this.name = forecastResponse.getName();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.lat);
        dest.writeString(this.lon);
        dest.writeString(this.description);
        dest.writeString(this.temp);
        dest.writeString(this.tempMax);
        dest.writeString(this.tempMin);
        dest.writeString(this.humidity);
        dest.writeString(this.pressure);
        dest.writeString(this.name);
        dest.writeInt(this.conditionId);
    }

    protected ForecastViewModel(Parcel in) {
        this.lat = in.readString();
        this.lon = in.readString();
        this.description = in.readString();
        this.temp = in.readString();
        this.tempMax = in.readString();
        this.tempMin = in.readString();
        this.humidity = in.readString();
        this.pressure = in.readString();
        this.name = in.readString();
        this.conditionId = in.readInt();
    }

    public static final Creator<ForecastViewModel> CREATOR = new Creator<ForecastViewModel>() {
        @Override
        public ForecastViewModel createFromParcel(Parcel source) {
            return new ForecastViewModel(source);
        }

        @Override
        public ForecastViewModel[] newArray(int size) {
            return new ForecastViewModel[size];
        }
    };

    @Override
    public String toString() {
        return "ForecastViewModel{" +
                "lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", description='" + description + '\'' +
                ", temp='" + temp + '\'' +
                ", tempMax='" + tempMax + '\'' +
                ", tempMin='" + tempMin + '\'' +
                ", humidity='" + humidity + '\'' +
                ", pressure='" + pressure + '\'' +
                ", name='" + name + '\'' +
                ", conditionId='" + conditionId + '\'' +
                '}';
    }
}
