package com.test.locationforecastmvp.util;

import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtilTest {
    String API_KEY = NetworkUtil.API_KEY;
    String FORECAST_REQUEST = NetworkUtil.FORECAST_REQUEST;

    @Test
    public void givenCoordinates_UrlGetterReturnsValid() {
        String validUrlStr = "https://api.openweathermap.org/data/2.5/weather?lat=56.327530&lon=44.000717&units=metric&APPID=ed3724d4850e07c530135c5488eba79c";
        try {
            URL validUrl = new URL(validUrlStr);
            Assert.assertEquals(validUrl.toString(), NetworkUtil.getLatLonRequestUrl("56.327530", "44.000717").toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
