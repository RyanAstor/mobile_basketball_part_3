package com.bignerdranch.android.project_1.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {
    @GET("data/2.5/weather?q=" +
            "Worcester," +
            "us" +
            "&appid=1dc6c314be1cb96dfc14e8050c60ee47")
    fun fetchWeather(): Call<OpenWeatherResponse>//(@Query("lat") lat: String, @Query("lon") lon: String, @Query("APPID") app_id: String): Call<OpenWeatherResponse>
}