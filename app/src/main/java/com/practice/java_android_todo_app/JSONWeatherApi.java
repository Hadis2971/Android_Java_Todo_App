package com.practice.java_android_todo_app;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JSONWeatherApi {
    @GET("v1/forecast?latitude=52.52&longitude=13.41&current_weather=true")
    Call<Weather> getWeather();
}
