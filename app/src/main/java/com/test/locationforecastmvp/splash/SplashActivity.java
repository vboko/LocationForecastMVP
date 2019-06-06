package com.test.locationforecastmvp.splash;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.test.locationforecastmvp.R;
import com.test.locationforecastmvp.forecast.ForecastActivity;

public class SplashActivity extends AppCompatActivity {
    private static final String LOG_TAG = "SplashActivity";

    private static final int SPLASH_DELAY = 700;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final String REQUIRED_LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkPermissions();
    }

    protected void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, REQUIRED_LOCATION_PERMISSION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{REQUIRED_LOCATION_PERMISSION},
                    REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            openForecastAfterDelay();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(LOG_TAG, "onRequestPermissionsResult: granted "
                            + REQUIRED_LOCATION_PERMISSION);
                    openForecastAfterDelay();
                } else {
                    // Exit the app
                    Toast.makeText(this, "Required permission '" + REQUIRED_LOCATION_PERMISSION
                            + "' not granted, exiting", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

    private void openForecastAfterDelay() {
        final Handler handler = new Handler();
        handler.postDelayed(this::proceedToForecast, SPLASH_DELAY);
    }

    private void proceedToForecast() {
        ForecastActivity.start(this);
    }
}
