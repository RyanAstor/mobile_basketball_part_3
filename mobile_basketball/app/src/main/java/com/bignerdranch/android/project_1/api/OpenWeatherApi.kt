package com.bignerdranch.android.project_1.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {
    @GET("data/2.5/weather?" +
            "lat=42.2625917" +
            "&lon=-71.8022933" +
            "&appid=1dc6c314be1cb96dfc14e8050c60ee47")
    fun fetchWeather(@Query("lat") lat: Double,
                     @Query("lon") lon: Double,
                     @Query("appid") app_id: String): Call<OpenWeatherResponse>
}