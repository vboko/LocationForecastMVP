package com.test.locationforecastmvp.forecast;

import com.google.gson.Gson;
import com.test.locationforecastmvp.model.data.CommonConstants;
import com.test.locationforecastmvp.model.data.response.ForecastResponse;
import com.test.locationforecastmvp.model.Repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class ForecastPresenterTest {

    @Mock
    private ForecastContract.View forecastView;

    @Mock
    private Repository repository;

    @Captor
    private ArgumentCaptor<Repository.RepositoryServiceCallback<ForecastResponse>> repositoryCallback;

    private ForecastPresenter forecastPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        forecastPresenter = new ForecastPresenter(forecastView, repository);
    }

    @Test
    public void loadForecast_FromNetwork_andSucceeded() throws Exception {
        ForecastResponse response = generateForecastResponse();

        forecastPresenter.getForecast();
        verify(forecastView).showLoading();
        verify(repository).getForecast(repositoryCallback.capture());

        repositoryCallback.getValue().onResultOk(response, CommonConstants.SERVICE_RESULT_NETWORK_OK);

        verify(forecastView).hideLoading();
        verify(forecastView).showForecast(response);
    }

    @Test
    public void loadForecast_FromDatabase_andSucceeded() throws Exception {
        ForecastResponse response = null;//generateForecastResponse();

        forecastPresenter.getForecast();
        verify(forecastView).showLoading();
        verify(repository).getForecast(repositoryCallback.capture());

        repositoryCallback.getValue().onResultOk(response, CommonConstants.SERVICE_RESULT_DB_OK);

        verify(forecastView).hideLoading();
        verify(forecastView).showForecast(response);
        verify(forecastView).showOfflineMessage(true);
    }

    @Test
    public void loadForecast_andFailed() throws Exception {
        String testError = "error";

        forecastPresenter.getForecast();
        verify(forecastView).showLoading();
        verify(repository).getForecast(repositoryCallback.capture());

        repositoryCallback.getValue().onError(testError);

        verify(forecastView).hideLoading();
        verify(forecastView).showError(testError);
    }

    @Test
    public void loadForecast_onNoConnectionAndEmptyDb_andShowMessage() throws Exception {
        forecastPresenter.getForecast();
        verify(forecastView).showLoading();
        verify(repository).getForecast(repositoryCallback.capture());

        repositoryCallback.getValue().onError(CommonConstants.ERROR_EMPTY_DB);

        verify(forecastView).hideLoading();
        verify(forecastView).showNoRecords(true);
    }

    @Test
    public void testCreated() throws Exception {
        assertNotNull(forecastPresenter);
    }

    @Test
    public void testNoActionsWithView() throws Exception {
        Mockito.verifyNoMoreInteractions(forecastView);
    }

    private ForecastResponse generateForecastResponse() {
//        String response = "{\"coord\":{\"lon\":43.99,\"lat\":56.33},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"base\":\"stations\",\"main\":{\"temp\":16.77,\"pressure\":1022,\"humidity\":59,\"temp_min\":16.67,\"temp_max\":17},\"visibility\":10000,\"wind\":{\"speed\":3,\"deg\":260},\"clouds\":{\"all\":0},\"dt\":1559775747,\"sys\":{\"type\":1,\"id\":9037,\"message\":0.0051,\"country\":\"RU\",\"sunrise\":1559780373,\"sunset\":1559843160},\"timezone\":10800,\"id\":520555,\"name\":\"Nizhniy Novgorod\",\"cod\":200}";
        String response = "asdfasdf";

        Gson gson = new Gson();
        ForecastResponse forecastResponse = gson.fromJson(response, ForecastResponse.class);

        return forecastResponse;
    }

}
