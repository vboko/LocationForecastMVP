package com.test.locationforecastmvp.forecast;

import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.test.locationforecastmvp.AppForecast;
import com.test.locationforecastmvp.model.ForecastApi;
import com.test.locationforecastmvp.model.data.response.ForecastResponse;
import com.test.locationforecastmvp.model.Repository;
import static android.content.Context.LOCATION_SERVICE;
import static com.test.locationforecastmvp.model.data.CommonConstants.ERROR_EMPTY_DB;
import static com.test.locationforecastmvp.model.data.CommonConstants.SERVICE_RESULT_DB_OK;
import static com.test.locationforecastmvp.model.data.CommonConstants.SERVICE_RESULT_NETWORK_OK;

public class ForecastPresenter implements ForecastContract.Presenter {
    private static final String LOG_TAG = "===P R E S E N T E R===";

    private final ForecastContract.View view;

    private Repository repository;


    public ForecastPresenter(@NonNull ForecastContract.View view, Repository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void getForecast() {
        Log.wtf(LOG_TAG, "getForecast: ------- getting forecast ------");

        // TODO: 2019-06-06 commented for tests
        /*if (!isGpsEnabled()) {
            view.showError("Please enable GPS");
        }*/

        view.showLoading();

        repository.getForecast(new ForecastApi.RepositoryServiceCallback<ForecastResponse>() {
            @Override
            public void onResultOk(ForecastResponse result, int resultCode) {
                Log.wtf(LOG_TAG, "onResultOk: callback");
                onRepositoryResultOk(resultCode, result);
            }

            @Override
            public void onError(String error) {
                Log.wtf(LOG_TAG, "onError: callback");
                onServiceError(error);
            }
        });
    }

    @Override
    public void stop() {
        Log.d(LOG_TAG, "stop: ");
        repository.onStop();
    }

    public void onRepositoryResultOk(int resultCode, ForecastResponse forecastResponse) {
        Log.wtf(LOG_TAG, "onRepositoryResultOk: code " + resultCode);
        switch (resultCode) {
            case SERVICE_RESULT_DB_OK:
                onServiceDbResult(forecastResponse);
                break;
            case SERVICE_RESULT_NETWORK_OK:
                onServiceNetworkResult(forecastResponse);
                break;
        }
    }

    private void onServiceResultOk(ForecastResponse response) {
        Log.wtf(LOG_TAG, "onServiceResultOk: ");
        view.hideLoading();
        view.showForecast(response);
    }

    private void onServiceNetworkResult(ForecastResponse response) {
        Log.wtf(LOG_TAG, "onServiceNetworkResult: ");
        onServiceResultOk(response);
        view.showOfflineMessage(false);
    }

    private void onServiceDbResult(ForecastResponse response) {
        Log.wtf(LOG_TAG, "onServiceDbResult: ");
        onServiceResultOk(response);
        // Couldn't retrieve data from the server due to network connectivity, show message
        view.showOfflineMessage(true);
    }

    private void onServiceError(String errorMessage) {
        Log.wtf(LOG_TAG, "onServiceError: ");
        view.hideLoading();
        view.showError(errorMessage);
        if (errorMessage.equals(ERROR_EMPTY_DB)) {
            Log.wtf(LOG_TAG, "onEmptyDb: ");
            view.showNoRecords(true);
            view.showOfflineMessage(true);
        }
    }

    private boolean isGpsEnabled() {
        boolean isEnabled = false;
        LocationManager service = (LocationManager) AppForecast.getInstance().getSystemService(LOCATION_SERVICE);
        if (service != null) {
            isEnabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        return isEnabled;
    }
}
