package com.antonageev.weatherapp;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.antonageev.weatherapp.model_current.WeatherRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import io.reactivex.subscribers.TestSubscriber;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private MockWebServer mockWebServer;
    private Retrofit retrofit;

    private IOpenWeatherRequest openWeatherRequest;


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.antonageev.weatherapp", appContext.getPackageName());
    }

    @Before
    public void prepare() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/").toString())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        openWeatherRequest = retrofit.create(IOpenWeatherRequest.class);
    }

    @Test
    public void getCurrentWeather() {
        mockWebServer.enqueue(getMockCorrectCurrentWeather());
        TestSubscriber<Boolean> testSubscriber = TestSubscriber.create();

        openWeatherRequest.loadWeather("Москва", "RU", WeatherDataLoader.API_KEY)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                        if (response.body() == null) {
                            testSubscriber.onNext(false);
                            testSubscriber.onComplete();
                            return;
                        }

                        boolean equals = String.valueOf(response.body().getDt()).equals("1592660724") && response.body().getName().equals("Москва");

                        testSubscriber.onNext(equals);
                        testSubscriber.onComplete();

                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
                        testSubscriber.onError(t);
                    }
                });
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValue(true);
    }



    private MockResponse getMockCorrectCurrentWeather() {
        return new MockResponse().setBody("{\n" +
                "    \"coord\": {\n" +
                "        \"lon\": 37.62,\n" +
                "        \"lat\": 55.75\n" +
                "    },\n" +
                "    \"weather\": [\n" +
                "        {\n" +
                "            \"id\": 200,\n" +
                "            \"main\": \"Thunderstorm\",\n" +
                "            \"description\": \"гроза с небольшим дождём\",\n" +
                "            \"icon\": \"11d\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"base\": \"stations\",\n" +
                "    \"main\": {\n" +
                "        \"temp\": 22,\n" +
                "        \"feels_like\": 24.69,\n" +
                "        \"temp_min\": 19,\n" +
                "        \"temp_max\": 27,\n" +
                "        \"pressure\": 1014,\n" +
                "        \"humidity\": 93\n" +
                "    },\n" +
                "    \"visibility\": 10000,\n" +
                "    \"wind\": {\n" +
                "        \"speed\": 2,\n" +
                "        \"deg\": 20\n" +
                "    },\n" +
                "    \"clouds\": {\n" +
                "        \"all\": 40\n" +
                "    },\n" +
                "    \"dt\": 1592660724,\n" +
                "    \"sys\": {\n" +
                "        \"type\": 1,\n" +
                "        \"id\": 9029,\n" +
                "        \"country\": \"RU\",\n" +
                "        \"sunrise\": 1592613868,\n" +
                "        \"sunset\": 1592677067\n" +
                "    },\n" +
                "    \"timezone\": 10800,\n" +
                "    \"id\": 524901,\n" +
                "    \"name\": \"Москва\",\n" +
                "    \"cod\": 200\n" +
                "}");
    }

    @After
    public void finish() throws IOException {
        mockWebServer.shutdown();
    }
}
