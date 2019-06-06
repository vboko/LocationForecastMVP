package com.test.locationforecastmvp.forecast;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.test.locationforecastmvp.R;
import com.test.locationforecastmvp.databinding.ActivityForecastBinding;
import com.test.locationforecastmvp.model.data.response.ForecastResponse;
import com.test.locationforecastmvp.model.data.viewmodel.ForecastViewModel;
import com.test.locationforecastmvp.model.location.UserLocationProvider;
import com.test.locationforecastmvp.model.Repository;

public class ForecastActivity extends AppCompatActivity implements ForecastContract.View {
    private static final String LOG_TAG = "ForecastActivity";

    private ForecastPresenter presenter;

    private ActivityForecastBinding viewBinding;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ForecastActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_forecast);
        presenter = new ForecastPresenter(this, new Repository(new UserLocationProvider()));

        Toolbar toolbar = viewBinding.toolbar;
        toolbar.setTitle(R.string.location_forecast);
        setSupportActionBar(toolbar);

        viewBinding.btnGetForecast.setOnClickListener(v -> onClickForecast());
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.getForecast();
    }

    @Override
    protected void onPause() {
        super.onPause();

        presenter.stop();
    }

    /*@Override
    public void showForecast(ForecastViewModel forecastViewModel) {
        // TODO: 2019-06-04 move to presenter
        viewBinding.setNoRecords(false);
        viewBinding.setForecastVM(forecastViewModel);
    }*/

    @Override
    public void showForecast(ForecastResponse forecastResponse) {
        // TODO: 2019-06-04 move to presenter
        viewBinding.setNoRecords(false);
        viewBinding.setForecastVM(new ForecastViewModel(forecastResponse));
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        Log.wtf(LOG_TAG, "showLoading: ");

        viewBinding.setIsLoading(true);
    }

    @Override
    public void hideLoading() {
        Log.wtf(LOG_TAG, "hideLoading: ");

        viewBinding.setIsLoading(false);
    }

    @Override
    public void showOfflineMessage(boolean isOffline) {
        viewBinding.setIsOffline(isOffline);
    }

    @Override
    public void showNoRecords(boolean isEmpty) {
        viewBinding.setNoRecords(isEmpty);
    }

    public void onClickForecast() {
        presenter.getForecast();
    }
}
