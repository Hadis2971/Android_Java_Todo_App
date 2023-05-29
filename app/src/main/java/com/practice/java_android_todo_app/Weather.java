package com.practice.java_android_todo_app;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("current_weather")
    private CurrentWeather currentWeather;

    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(CurrentWeather currentWeather) {
        this.currentWeather = currentWeather;
    }


}
