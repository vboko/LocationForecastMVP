package com.test.locationforecastmvp.model;

import com.test.locationforecastmvp.model.data.response.ForecastResponse;

public interface ForecastApi {

    interface RepositoryServiceCallback<T> {
        void onResultOk(T result, int resultCode);
        void onError(String error);
    }

    void getForecast(RepositoryServiceCallback<ForecastResponse> callback);

}
