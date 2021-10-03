package com.bignerdranch.android.project_1.api

import retrofit2.Call
import retrofit2.http.GET

interface OpenWeatherApi {
    @GET("data/2.5/weather?q=" +
            "Worcester," +
            "us" +
            "&appid=1dc6c314be1cb96dfc14e8050c60ee47")
    fun fetchWeather(): Call<OpenWeatherResponse>
}