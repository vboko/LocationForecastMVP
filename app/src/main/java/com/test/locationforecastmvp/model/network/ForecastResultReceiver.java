package com.test.locationforecastmvp.model.network;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.ResultReceiver;

public class ForecastResultReceiver extends ResultReceiver implements Parcelable {

    public interface Receiver {
        void onReceiveResult(int resultCode, Bundle resultData);
    }

    private Receiver receiver;

    public ForecastResultReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (receiver != null) {
            receiver.onReceiveResult(resultCode, resultData);
        }
    }
}
