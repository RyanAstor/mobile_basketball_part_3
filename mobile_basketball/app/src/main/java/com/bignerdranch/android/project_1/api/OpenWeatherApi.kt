package com.bignerdranch.android.project_1.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface OpenWeatherApi {
    @GET
    fun fetchWeather(@Url url:String): Call<OpenWeatherResponse>
}